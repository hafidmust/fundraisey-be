package com.fundraisey.backend.service.implementation.investor;

import com.fundraisey.backend.entity.auth.User;
import com.fundraisey.backend.entity.investor.Investor;
import com.fundraisey.backend.entity.startup.Loan;
import com.fundraisey.backend.entity.startup.LoanStatus;
import com.fundraisey.backend.entity.transaction.*;
import com.fundraisey.backend.model.TransactionRequestModel;
import com.fundraisey.backend.repository.auth.UserRepository;
import com.fundraisey.backend.repository.investor.InvestorRepository;
import com.fundraisey.backend.repository.investor.PaymentAgentRepository;
import com.fundraisey.backend.repository.investor.ReturnInstallmentRepository;
import com.fundraisey.backend.repository.investor.TransactionRepository;
import com.fundraisey.backend.repository.startup.LoanRepository;
import com.fundraisey.backend.service.interfaces.investor.TransactionService;
import com.fundraisey.backend.util.ResponseTemplate;
import com.fundraisey.backend.util.SimpleStringUtils;
import org.hibernate.type.CalendarTimeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class TransactionImplementation implements TransactionService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    InvestorRepository investorRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    LoanRepository loanRepository;
    @Autowired
    PaymentAgentRepository paymentAgentRepository;
    @Autowired
    ResponseTemplate responseTemplate;
    @Autowired
    ReturnInstallmentRepository returnInstallmentRepository;
    @Autowired
    SimpleStringUtils simpleStringUtils;

    @Override
    public Map insert(String email, TransactionRequestModel transactionRequestModel) {
        try {
            if (transactionRequestModel.getAmount() <= 0) return responseTemplate.notAllowed("Transaction amount " +
                    "can't be 0 or less");
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, 5);
            Date paymentDeadline = calendar.getTime();

            User user = userRepository.findOneByEmail(email);
            Investor investor = investorRepository.findByUser(user);
            if (investor == null) return responseTemplate.notFound("Investor not found");
            Loan loan = loanRepository.getById(transactionRequestModel.getLoanId());
            if (loan == null) return responseTemplate.notFound("Loan not found");
            if (loan.getStatus() == LoanStatus.pending) return responseTemplate.notAllowed("Loan acceptance pending");
            if (loan.getStatus() == LoanStatus.rejected) return responseTemplate.notAllowed("Loan acceptance rejected");
            if (loan.getStatus() == LoanStatus.withdrawn) return responseTemplate.notAllowed("Loan already withdrawn");
            PaymentAgent paymentAgent = paymentAgentRepository.getById(transactionRequestModel.getPaymentAgentId());
            if (paymentAgent == null) return responseTemplate.notFound(("Payment agent not found"));
            if ((transactionRequestModel.getAmount() +
                    transactionRepository.sumOfPaidTransactionByLoanId(loan.getId())) > loan.getTargetValue())
                return responseTemplate.notAllowed("Total of current loan value and transaction amount exceed the target value");

            Transaction transaction = new Transaction();
            transaction.setInvestor(investor);
            transaction.setAmount(transactionRequestModel.getAmount());
            transaction.setPaymentAgent(paymentAgent);
            transaction.setLoan(loan);
            transaction.setAccountNumber(simpleStringUtils.randomString(12,true));
            transaction.setTransactionStatus(TransactionStatus.pending);
            transaction.setPaymentDeadline(paymentDeadline);

            Transaction savedTransaction = transactionRepository.save(transaction);

            return responseTemplate.success(transactionRepository.getById(savedTransaction.getId()));
        } catch (Exception e) {
            e.printStackTrace();
            return responseTemplate.internalServerError(e);
        }
    }

    @Override
    public Map pay(String email, TransactionRequestModel transactionRequestModel) {
        try {
            User user = userRepository.findOneByEmail(email);
            Investor investor = investorRepository.findByUser(user);
            Transaction transaction = transactionRepository.getById(transactionRequestModel.getTransactionId());

            if (transaction.getInvestor().getId() != investor.getId()) return responseTemplate.notAllowed("Not the " +
                    "owner transaction");
            if (investor == null) return responseTemplate.notFound("Investor not found");
            if (transaction.getTransactionStatus() == TransactionStatus.paid) return responseTemplate.notAllowed("Already paid");

            addReturnInstallment(transaction);
            transaction.setTransactionStatus(TransactionStatus.paid);

            Transaction savedTransaction = transactionRepository.save(transaction);

            return responseTemplate.success(savedTransaction);
        } catch (Exception e) {
            e.printStackTrace();
            return responseTemplate.internalServerError(e);
        }
    }

    private void addReturnInstallment(Transaction transaction) {
        Integer totalReturnPeriod = transaction.getLoan().getTotalReturnPeriod();
        Float interestRate = transaction.getLoan().getInterestRate();
        Float amountReturned =
                (transaction.getAmount().floatValue() +
                        (transaction.getAmount().floatValue() * interestRate / 100)) / totalReturnPeriod;
        String paymentPlan = transaction.getLoan().getPaymentPlan().getName();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(transaction.getLoan().getEndDate());
        for (Integer i = 1; i <= totalReturnPeriod; i++) {
            ReturnInstallment returnInstallment = new ReturnInstallment();
            if (paymentPlan.equals("cash") && (totalReturnPeriod == 1)) {
                calendar.add(Calendar.YEAR, 2);
                returnInstallment.setReturnDate(calendar.getTime());
            } else if (paymentPlan.equals("per1year") && (totalReturnPeriod == 2)) {
                calendar.add(Calendar.YEAR, 1);
                returnInstallment.setReturnDate(calendar.getTime());
            } else if (paymentPlan.equals("per6months") && (totalReturnPeriod == 4)) {
                calendar.add(Calendar.MONTH, 6);
                returnInstallment.setReturnDate(calendar.getTime());
            }
            returnInstallment.setTransaction(transaction);
            returnInstallment.setReturnPeriod(i);
            returnInstallment.setReturnStatus(ReturnStatus.unpaid);
            returnInstallment.setTotalReturnPeriod(totalReturnPeriod);
            returnInstallment.setAmount(amountReturned.longValue());

            returnInstallmentRepository.save(returnInstallment);
        }
    }

    @Override
    public Map update(Transaction transaction) {
        return null;
    }

    @Override
    public Map getByEmail(Integer page, Integer size, String sortAttribute, String sortType, String email) {
        Page<Transaction> transactions;
        Pageable pageable;
        sortAttribute = sortAttribute.equals("") ? "id" : sortAttribute;
        try {
            User user = userRepository.findOneByEmail(email);
            Investor investor = investorRepository.findByUser(user);

            if ((sortType == "desc") || (sortType == "descending")) {
                pageable = PageRequest.of(page, size, Sort.by(sortAttribute).descending());
            } else {
                pageable = PageRequest.of(page, size, Sort.by(sortAttribute).ascending());
            }

            transactions = transactionRepository.findByInvestor(investor, pageable);

            return responseTemplate.success(transactions);
        } catch (Exception e) {
            e.printStackTrace();
            return responseTemplate.internalServerError(e);
        }
    }

    @Override
    public Map getByUserId(Integer page, Integer size, String sortAttribute, String sortType, Long userId) {
        try {
            User user = userRepository.getById(userId);
            return getByEmail(page, size, sortAttribute, sortType, user.getEmail());
        } catch (Exception e) {
            e.printStackTrace();
            return responseTemplate.internalServerError(e);
        }
    }

    @Secured("ROLE_ADMIN")
    @Override
    public Map getAll() {
        try {
            List<Transaction> transactions = transactionRepository.getAll();
            return responseTemplate.success(transactions);
        } catch (Exception e) {
            e.printStackTrace();
            return responseTemplate.internalServerError(e);
        }
    }

    @Override
    public Map getById(Long id) {
        try {
            Transaction transaction = transactionRepository.getById(id);

            return responseTemplate.success(transaction);
        } catch (Exception e) {
            e.printStackTrace();
            return responseTemplate.internalServerError(e);
        }
    }
}
