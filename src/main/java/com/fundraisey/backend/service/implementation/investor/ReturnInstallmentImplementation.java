package com.fundraisey.backend.service.implementation.investor;

import com.fundraisey.backend.entity.auth.User;
import com.fundraisey.backend.entity.investor.Investor;
import com.fundraisey.backend.entity.startup.Loan;
import com.fundraisey.backend.entity.startup.Startup;
import com.fundraisey.backend.entity.transaction.*;
import com.fundraisey.backend.model.PortofolioModel;
import com.fundraisey.backend.model.PortofolioSummaryModel;
import com.fundraisey.backend.repository.auth.UserRepository;
import com.fundraisey.backend.repository.investor.InvestorRepository;
import com.fundraisey.backend.repository.investor.ReturnInstallmentRepository;
import com.fundraisey.backend.repository.investor.ReturnInvoiceRepository;
import com.fundraisey.backend.repository.investor.TransactionRepository;
import com.fundraisey.backend.repository.startup.LoanRepository;
import com.fundraisey.backend.repository.startup.StartupRepository;
import com.fundraisey.backend.service.interfaces.investor.ReturnInstallmentService;
import com.fundraisey.backend.util.ResponseTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ReturnInstallmentImplementation implements ReturnInstallmentService {
    @Autowired
    ReturnInstallmentRepository returnInstallmentRepository;
    @Autowired
    InvestorRepository investorRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    LoanRepository loanRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    ReturnInvoiceRepository returnInvoiceRepository;
    @Autowired
    ResponseTemplate responseTemplate;

    @Override
    public Map getPortofolio(String email) {
        try {
            User user = userRepository.findOneByEmail(email);
            Investor investor = investorRepository.findByUser(user);
            List<Transaction> transactions = transactionRepository.getByInvestorId(investor.getId());
            List<PortofolioModel> portofolioModels = new ArrayList<>();

            for (Transaction transaction : transactions) {
                if (transaction.getTransactionStatus() != TransactionStatus.paid) continue;
                List<Transaction> loanTransaction = new ArrayList<>();
                loanTransaction.add(transaction);
                Loan loan = loanRepository.findByTransactionsIn(loanTransaction);
                Startup startup = loan.getStartup();

                PortofolioModel portofolioModel = new PortofolioModel();
                portofolioModel.setTransactionId(transaction.getId());
                portofolioModel.setStartupId(startup.getId());
                portofolioModel.setStartupName(startup.getName());
                portofolioModel.setLoanId(loan.getId());
                portofolioModel.setLoanName(loan.getName());
                portofolioModel.setStartDate(loan.getStartDate());
                portofolioModel.setEndDate(loan.getEndDate());
                portofolioModel.setCurrentLoanValue(transactionRepository.sumOfPaidTransactionByLoanId(loan.getId()));
                portofolioModel.setTargetLoanValue(loan.getTargetValue());
                portofolioModel.setPaymentPlanId(loan.getPaymentPlan().getId());
                portofolioModel.setPaymentPlanName(loan.getPaymentPlan().getName());
                portofolioModel.setReturnInstallment(returnInstallmentRepository.findByTransactionOrderByIdAsc(transaction));

                portofolioModels.add(portofolioModel);
            }

            return responseTemplate.success(portofolioModels);
        } catch (Exception e) {
            e.printStackTrace();
            return responseTemplate.internalServerError(e);
        }
    }

    @Override
    public Map getPortofolioSummary(String email) {
        try {
            User user = userRepository.findOneByEmail(email);
            Investor investor = investorRepository.findByUser(user);
            PortofolioSummaryModel portofolioSummaryModel = new PortofolioSummaryModel();

            Calendar calendar = Calendar.getInstance();

            Long totalWithdrawn = returnInvoiceRepository.getAmountWithdrawnSumByInvestorId(investor.getId());
            if (totalWithdrawn == null) totalWithdrawn = 0L;
            Long totalWithdrawnThisMonth =
                    returnInvoiceRepository.getAmountWithdrawnSumByYearAndMonth(investor.getId(),
                            calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);
            if (totalWithdrawnThisMonth == null) totalWithdrawnThisMonth = 0L;
            Long totalFunding = transactionRepository.sumOfPaidTransactionByInvestorId(investor.getId());
            if (totalFunding == null) totalFunding = 0L;
            Long totalReturn = returnInstallmentRepository.getAmountSumByInvestorId(investor.getId());
            if (totalReturn == null) totalReturn = 0L;
            Long upcomingReturn = returnInstallmentRepository.getAmountSumByReturnStatus(ReturnStatus.unpaid);
            if (upcomingReturn == null) upcomingReturn = 0L;
            Integer loanTransactionCount = transactionRepository.countOfPaidTransactionByInvestorId(investor.getId());
            if (loanTransactionCount == null) loanTransactionCount = 0;
            Long balance = returnInstallmentRepository.getAmountSumOfAllPaidAndNotWithdrawnReturnByUserEmail(email);
            if (balance == null) balance = 0L;

            portofolioSummaryModel.setTotalWithdrawn(totalWithdrawn);
            portofolioSummaryModel.setTotalWithdrawnThisMonth(totalWithdrawnThisMonth);
            portofolioSummaryModel.setTotalFunding(totalFunding);
            portofolioSummaryModel.setTotalReturn(totalReturn);
            portofolioSummaryModel.setUpcomingReturn(upcomingReturn);
            portofolioSummaryModel.setLoanTransactionCount(loanTransactionCount);
            portofolioSummaryModel.setBalance(balance);

            return responseTemplate.success(portofolioSummaryModel);

        } catch (Exception e) {
            e.printStackTrace();
            return responseTemplate.internalServerError(e);
        }
    }

    @Override
    public Map withdraw(String email, Long returnInstallmentId) {
        try {
            User user = userRepository.findOneByEmail(email);

            ReturnInstallment returnInstallment = returnInstallmentRepository.getById(returnInstallmentId);
            if (returnInstallment == null) return responseTemplate.notFound("No return installment found");
            if (user.getId() != returnInstallment.getTransaction().getInvestor().getUser().getId())
                return responseTemplate.notAllowed("Not the owner of return installment");


            if (returnInstallment.getReturnStatus() != ReturnStatus.paid) return responseTemplate.notAllowed("Not returned yet");
            if (returnInstallment.isWithdrawn()) return responseTemplate.notAllowed("Already withdrawn");

            ReturnInvoice returnInvoice = new ReturnInvoice();
            returnInvoice.setAmount(returnInstallment.getAmount());
            returnInvoice.setPaymentDate(new Date());
            returnInvoice.setReturnInstallment(returnInstallment);

            returnInvoiceRepository.save(returnInvoice);
            returnInstallment.setWithdrawn(true);
            ReturnInstallment saved = returnInstallmentRepository.save(returnInstallment);
            return responseTemplate.success(saved);
        } catch (Exception e) {
            e.printStackTrace();
            return responseTemplate.internalServerError(e);
        }

    }

    @Override
    public Map withdrawAll(String email) {
        Map<String, Object> response = new HashMap<>();

        try {
            List<ReturnInstallment> returnInstallments =
                    returnInstallmentRepository.getAllPaidAndNotWithdrawnReturnByUserEmail(email);
            if (returnInstallments.size() == 0) return responseTemplate.notFound("Nothing to withdraw");

            Integer withdrawSuccessCount = 0;
            Integer withdrawFailCount = 0;
            Long withdrawSum = 0L;

            for (ReturnInstallment returnInstallment : returnInstallments) {
                if (returnInstallment.getReturnStatus() != ReturnStatus.paid) withdrawFailCount++;
                if (returnInstallment.isWithdrawn()) withdrawFailCount++;

                ReturnInvoice returnInvoice = new ReturnInvoice();
                returnInvoice.setAmount(returnInstallment.getAmount());
                returnInvoice.setPaymentDate(new Date());
                returnInvoice.setReturnInstallment(returnInstallment);

                returnInvoiceRepository.save(returnInvoice);
                returnInstallment.setWithdrawn(true);
                ReturnInstallment saved = returnInstallmentRepository.save(returnInstallment);
                withdrawSuccessCount++;
                withdrawSum = withdrawSum + returnInstallment.getAmount();
            }
            response.put("withdraw success count", withdrawSuccessCount);
            response.put("withdraw fail count", withdrawFailCount);
            response.put("total withdrawn", withdrawSum);

            return responseTemplate.success(response);
        } catch (Exception e) {
            e.printStackTrace();
            return responseTemplate.internalServerError(e);
        }

    }
}
