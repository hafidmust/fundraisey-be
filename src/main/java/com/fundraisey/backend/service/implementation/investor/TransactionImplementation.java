package com.fundraisey.backend.service.implementation.investor;

import com.fundraisey.backend.entity.auth.User;
import com.fundraisey.backend.entity.investor.Investor;
import com.fundraisey.backend.entity.startup.Loan;
import com.fundraisey.backend.entity.startup.LoanStatus;
import com.fundraisey.backend.entity.startup.Payment;
import com.fundraisey.backend.entity.transaction.*;
import com.fundraisey.backend.model.TransactionRequestModel;
import com.fundraisey.backend.repository.auth.UserRepository;
import com.fundraisey.backend.repository.investor.InvestorRepository;
import com.fundraisey.backend.repository.investor.PaymentAgentRepository;
import com.fundraisey.backend.repository.investor.ReturnInstallmentRepository;
import com.fundraisey.backend.repository.investor.TransactionRepository;
import com.fundraisey.backend.repository.startup.LoanRepository;
import com.fundraisey.backend.repository.startup.PaymentRepository;
import com.fundraisey.backend.service.implementation.auth.LoginImplementation;
import com.fundraisey.backend.service.interfaces.investor.TransactionService;
import com.fundraisey.backend.util.ResponseTemplate;
import com.fundraisey.backend.util.SimpleStringUtils;
import org.hibernate.type.CalendarTimeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.util.*;

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
    PaymentRepository paymentRepository;
    @Autowired
    SimpleStringUtils simpleStringUtils;

    Logger logger = LoggerFactory.getLogger(LoginImplementation.class);

    @Override
    public Map insert(String email, TransactionRequestModel transactionRequestModel) {
        try {
            Loan loan = loanRepository.getById(transactionRequestModel.getLoanId());
            if (loan == null) return responseTemplate.notFound("Loan not found");
            Calendar calendar = Calendar.getInstance();
            Integer timeComparison = calendar.getTime().compareTo(loan.getEndDate());
            if (timeComparison == 1) return responseTemplate.notAllowed("Funding period is over");

            if (transactionRequestModel.getAmount() <= 0) return responseTemplate.notAllowed("Transaction amount " +
                    "can't be 0 or less");
            calendar.add(Calendar.DATE, 5);
            Date paymentDeadline = calendar.getTime();

            User user = userRepository.findOneByEmail(email);
            Investor investor = investorRepository.findByUser(user);
            if (investor == null) return responseTemplate.notFound("Investor not found");
            if (loan.getStatus() == LoanStatus.pending) return responseTemplate.notAllowed("Loan acceptance pending");
            if (loan.getStatus() == LoanStatus.rejected) return responseTemplate.notAllowed("Loan acceptance rejected");
            if (loan.getStatus() == LoanStatus.withdrawn) return responseTemplate.notAllowed("Loan already withdrawn");
            PaymentAgent paymentAgent = paymentAgentRepository.getById(transactionRequestModel.getPaymentAgentId());
            if (paymentAgent == null) return responseTemplate.notFound(("Payment agent not found"));

            // Check if total current value + new transaction amount exceed total value
            Long paidSum = transactionRepository.sumOfPaidTransactionByLoanId(loan.getId());
            if (paidSum == null) paidSum = 0L;
            if ((transactionRequestModel.getAmount() + paidSum) > loan.getTargetValue())
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

            Calendar calendar = Calendar.getInstance();
            Integer timeComparison = calendar.getTime().compareTo(transaction.getPaymentDeadline());
            if (timeComparison == 1) return responseTemplate.notAllowed("Payment deadline exceeded");

            if (transaction == null) return responseTemplate.notFound("Transaction not found");
            if (transaction.getInvestor().getId() != investor.getId()) return responseTemplate.notAllowed("Not the " +
                    "owner transaction");
            if (investor == null) return responseTemplate.notFound("Investor not found");
            if (transaction.getTransactionStatus() == TransactionStatus.paid) return responseTemplate.notAllowed("Already paid");

            // Check if total current value + new transaction amount exceed total value
            List<Transaction> transactions = new ArrayList<>();
            transactions.add(transaction);
            Loan loan = loanRepository.findByTransactionsIn(transactions);
            if (loan.getStatus() == LoanStatus.withdrawn) return responseTemplate.notAllowed("Loan already withdrawn");
            Long paidSum = transactionRepository.sumOfPaidTransactionByLoanId(loan.getId());
            if (paidSum == null) paidSum = 0L;
            if ((transaction.getAmount() + paidSum) > loan.getTargetValue())
                return responseTemplate.notAllowed("Total of current loan value and transaction amount exceed the target value");

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

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(transaction.getLoan().getEndDate());
        for (Integer period = 1; period <= totalReturnPeriod; period++) {
            Payment payment = paymentRepository.getByLoanIdAndPeriod(transaction.getLoan().getId(), period);

            ReturnInstallment returnInstallment = new ReturnInstallment();
            returnInstallment.setPayment(payment);
            returnInstallment.setTransaction(transaction);
            returnInstallment.setReturnStatus(ReturnStatus.unpaid);
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

            if ((sortType.equals("desc")) || (sortType.equals("descending"))) {
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
