package com.fundraisey.backend.service.implementation.investor;

import com.fundraisey.backend.entity.auth.User;
import com.fundraisey.backend.entity.investor.Investor;
import com.fundraisey.backend.entity.startup.Loan;
import com.fundraisey.backend.entity.startup.Startup;
import com.fundraisey.backend.entity.transaction.*;
import com.fundraisey.backend.model.PortofolioModel;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    public Map withdraw(Long returnInstallmentId) {
        try {
            ReturnInstallment returnInstallment = returnInstallmentRepository.getById(returnInstallmentId);

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
}
