package com.fundraisey.backend.service.implementation.startup;

import com.fundraisey.backend.entity.auth.User;
import com.fundraisey.backend.entity.investor.Investor;
import com.fundraisey.backend.entity.startup.Loan;
import com.fundraisey.backend.entity.startup.PaymentPlan;
import com.fundraisey.backend.entity.startup.Startup;
import com.fundraisey.backend.model.LoanDetailModel;
import com.fundraisey.backend.model.LoanRequestModel;
import com.fundraisey.backend.repository.auth.UserRepository;
import com.fundraisey.backend.repository.investor.InvestorRepository;
import com.fundraisey.backend.repository.investor.TransactionRepository;
import com.fundraisey.backend.repository.startup.LoanRepository;
import com.fundraisey.backend.repository.startup.PaymentPlanRepository;
import com.fundraisey.backend.repository.startup.StartupRepository;
import com.fundraisey.backend.service.interfaces.startup.LoanService;
import com.fundraisey.backend.util.ResponseTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LoanImplementation implements LoanService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    LoanRepository loanRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    StartupRepository startupRepository;
    @Autowired
    PaymentPlanRepository paymentPlanRepository;
    @Autowired
    ResponseTemplate responseTemplate;

    public Map insert(String email, LoanRequestModel loanRequestModel) {
        try {
            if (loanRequestModel.getTitle() == null || loanRequestModel.getTitle().equals(""))
                return responseTemplate.isRequired("Title required");
            if (loanRequestModel.getTargetValue() == null || loanRequestModel.getTargetValue().equals(""))
                return responseTemplate.isRequired("Target value required");
            if (loanRequestModel.getEndDate() == null || loanRequestModel.getEndDate().equals(""))
                return responseTemplate.isRequired("End date required");
            if (loanRequestModel.getInterestRate() == null)
                return responseTemplate.isRequired("Interest rate required");
            if (loanRequestModel.getPaymentPlanId() == null)
                return responseTemplate.isRequired("Payment plan id required");

            User user = userRepository.findOneByEmail(email);
            Startup startup = startupRepository.findByUser(user);
            PaymentPlan paymentPlan = paymentPlanRepository.getById(loanRequestModel.getPaymentPlanId());

            Calendar calendar = Calendar.getInstance();
            Date startDate = calendar.getTime();

            Loan loan = new Loan();

            loan.setStartup(startup);
            loan.setName(loanRequestModel.getTitle());
            loan.setTargetValue(loanRequestModel.getTargetValue());
            loan.setDescription(loanRequestModel.getDescription());
            loan.setStartDate(new Date());
            loan.setEndDate(loanRequestModel.getEndDate());
            loan.setInterestRate(loanRequestModel.getInterestRate());
            loan.setPaymentPlan(paymentPlan);

            if (paymentPlan == null) return responseTemplate.notFound("Payment plan not found");
            if (paymentPlan.getName().equals("cash")) {
                loan.setTotalReturnPeriod(1);
            } else if (paymentPlan.getName().equals("1year") || paymentPlan.getName().equals("per1year")) {
                loan.setTotalReturnPeriod(2);
            } else if (paymentPlan.getName().equals("6months") || paymentPlan.getName().equals("per6months")) {
                loan.setTotalReturnPeriod(4);
            } else {
                return responseTemplate.notFound("Payment plan not found");
            }

            Loan loanObj = loanRepository.save(loan);

            return responseTemplate.success(loanObj);
        } catch (Exception e) {
            e.printStackTrace();
            return responseTemplate.internalServerError(e);
        }
    }

    @Override
    public Map getAllByEmail(Integer page, Integer size, String sortAttribute, String sortType, String email) {
        Page<Loan> loans;
        Pageable pageable;
        sortAttribute = sortAttribute.equals("") ? "id" : sortAttribute;
        try {
            User user = userRepository.findOneByEmail(email);
            Startup startup = startupRepository.findByUser(user);
            List<LoanDetailModel> response = new ArrayList<>();

            if ((sortType == "desc") || (sortType == "descending")) {
                pageable = PageRequest.of(page, size, Sort.by(sortAttribute).descending());
            } else {
                pageable = PageRequest.of(page, size, Sort.by(sortAttribute).ascending());
            }

            loans = loanRepository.findByStartup(startup, pageable);
            for (Loan loan : loans.getContent()) {
                LoanDetailModel loanDetailModel = createLoanDetailModel(loan);

                response.add(loanDetailModel);
            }

            return responseTemplate.success(response);
        } catch (Exception e) {
            e.printStackTrace();
            return responseTemplate.internalServerError(e);
        }
    }

    @Override
    public Map getAll(Integer page, Integer size, String sortAttribute, String sortType) {
        Page<Loan> loans;
        Pageable pageable;
        sortAttribute = sortAttribute.equals("") ? "id" : sortAttribute;
        try {
            List<LoanDetailModel> response = new ArrayList<>();

            if ((sortType == "desc") || (sortType == "descending")) {
                pageable = PageRequest.of(page, size, Sort.by(sortAttribute).descending());
            } else {
                pageable = PageRequest.of(page, size, Sort.by(sortAttribute).ascending());
            }

            loans = loanRepository.findAll(pageable);
            for (Loan loan : loans.getContent()) {
                LoanDetailModel loanDetailModel = createLoanDetailModel(loan);

                response.add(loanDetailModel);
            }

            return responseTemplate.success(response);
        } catch (Exception e) {
            e.printStackTrace();
            return responseTemplate.internalServerError(e);
        }
    }

    @Override
    public Map getById(Long id) {
        try {
            Loan loan = loanRepository.getById(id);

            LoanDetailModel loanDetailModel = createLoanDetailModel(loan);

            return responseTemplate.success(loanDetailModel);
        } catch (Exception e) {
            e.printStackTrace();
            return responseTemplate.internalServerError(e);
        }
    }

    private LoanDetailModel createLoanDetailModel(Loan loan) {
        Long currentValue = transactionRepository.sumOfPaidTransactionByLoanId(loan.getId());
        if (currentValue == null) currentValue = 0L;

        LoanDetailModel loanDetailModel = new LoanDetailModel();
        loanDetailModel.setId(loan.getId());
        loanDetailModel.setName(loan.getName());
        loanDetailModel.setDescription(loan.getDescription());
        loanDetailModel.setStartDate(loan.getStartDate());
        loanDetailModel.setEndDate(loan.getEndDate());
        loanDetailModel.setTargetValue(loan.getTargetValue());
        loanDetailModel.setCurrentValue(currentValue);
        loanDetailModel.setInterestRate(loan.getInterestRate());
        loanDetailModel.setStartup(loan.getStartup());

        return loanDetailModel;
    }
}
