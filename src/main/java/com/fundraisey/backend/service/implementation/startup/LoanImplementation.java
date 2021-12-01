package com.fundraisey.backend.service.implementation.startup;

import com.fundraisey.backend.entity.auth.User;
import com.fundraisey.backend.entity.investor.Investor;
import com.fundraisey.backend.entity.startup.*;
import com.fundraisey.backend.entity.transaction.ReturnInstallment;
import com.fundraisey.backend.entity.transaction.ReturnStatus;
import com.fundraisey.backend.model.LoanDetailModel;
import com.fundraisey.backend.model.LoanRequestModel;
import com.fundraisey.backend.model.StartupPaymentListModel;
import com.fundraisey.backend.repository.auth.UserRepository;
import com.fundraisey.backend.repository.investor.InvestorRepository;
import com.fundraisey.backend.repository.investor.ReturnInstallmentRepository;
import com.fundraisey.backend.repository.investor.TransactionRepository;
import com.fundraisey.backend.repository.startup.*;
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
    @Autowired
    ReturnInstallmentRepository returnInstallmentRepository;
    @Autowired
    PaymentInvoiceRepository paymentInvoiceRepository;
    @Autowired
    WithdrawalInvoiceRepository withdrawalInvoiceRepository;

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

            Loan loan = new Loan();

            loan.setStartup(startup);
            loan.setName(loanRequestModel.getTitle());
            loan.setTargetValue(loanRequestModel.getTargetValue());
            loan.setDescription(loanRequestModel.getDescription());
            loan.setStartDate(new Date());
            loan.setEndDate(loanRequestModel.getEndDate());
            loan.setInterestRate(loanRequestModel.getInterestRate());
            loan.setPaymentPlan(paymentPlan);
            loan.setStatus(LoanStatus.pending);

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
    public Map getAllByEmail(Integer page, Integer size, String sortAttribute, String sortType,
                             String search, String email) {
        Page<Loan> loans;
        Pageable pageable;
        sortAttribute = sortAttribute.equals("") ? "id" : sortAttribute;
        try {
            User user = userRepository.findOneByEmail(email);
            Startup startup = startupRepository.findByUser(user);
            List<LoanDetailModel> response = new ArrayList<>();

            if ((sortType.equals("desc")) || (sortType.equals("descending"))) {
                pageable = PageRequest.of(page, size, Sort.by(sortAttribute).descending());
            } else {
                pageable = PageRequest.of(page, size, Sort.by(sortAttribute).ascending());
            }

            loans = loanRepository.findByStartupAndNameContainingIgnoreCase(startup, search,pageable);
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
    public Map getAllAccepted(Integer page, Integer size, String sortAttribute, String sortType, String search) {
        Page<Loan> loans;
        Pageable pageable;
        sortAttribute = sortAttribute.equals("") ? "id" : sortAttribute;
        try {
            List<LoanDetailModel> response = new ArrayList<>();

            if ((sortType.equals("desc")) || (sortType.equals("descending"))) {
                pageable = PageRequest.of(page, size, Sort.by(sortAttribute).descending());
            } else {
                pageable = PageRequest.of(page, size, Sort.by(sortAttribute).ascending());
            }

//            loans = loanRepository.findByStatus(LoanStatus.accepted, pageable);
            loans = loanRepository.getAcceptedLoanByNameContainingOrStartupNameContaining(search, pageable);
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

    @Override
    public Map pay(String email, Long loanId, Integer period) {
        try {
            Startup startup = startupRepository.getByUserEmail(email);
            Loan loan = loanRepository.getById(loanId);

            if (startup.getId() != loan.getStartup().getId()) return responseTemplate.notAllowed("Not loan owner");

            ReturnInstallment returnInstallment =
                    returnInstallmentRepository.getByLoanIdAndPeriod(loanId, period);
            if (returnInstallment.getReturnStatus() == ReturnStatus.paid) {
                return responseTemplate.notAllowed("Already paid");
            } else if (returnInstallment.getReturnStatus() != ReturnStatus.paid) {
                // Create startup's payment invoice
                PaymentInvoice paymentInvoice = new PaymentInvoice();
                paymentInvoice.setPaymentDate(new Date());
                paymentInvoice.setReturnInstallment(returnInstallment);
                paymentInvoice.setAmount(returnInstallment.getAmount());
                paymentInvoiceRepository.save(paymentInvoice);

                // Change return installment status to paid
                returnInstallment.setReturnStatus(ReturnStatus.paid);
                returnInstallmentRepository.save(returnInstallment);
            }

            return responseTemplate.success(null);

        } catch (Exception e) {
            e.printStackTrace();
            return responseTemplate.internalServerError(e);
        }
    }

    @Override
    public Map getPaymentList(Long loanId) {
        try {
            Loan loan = loanRepository.getById(loanId);
            Integer totalReturnPeriod = loan.getTotalReturnPeriod();
            List<StartupPaymentListModel> paymentList = new ArrayList<>();

            // Create payment date(s)
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(loan.getEndDate());
            for (Integer period = 1; period <= totalReturnPeriod; period++) {
                StartupPaymentListModel payment = new StartupPaymentListModel();
                payment.setPeriod(period);
                if (totalReturnPeriod == 1) {
                    calendar.add(Calendar.YEAR, 2);
                    payment.setReturnDate(calendar.getTime());
                } else if (totalReturnPeriod == 2) {
                    calendar.add(Calendar.YEAR, 1);
                    payment.setReturnDate(calendar.getTime());
                } else if (totalReturnPeriod == 4) {
                    calendar.add(Calendar.MONTH, 6);
                    payment.setReturnDate(calendar.getTime());
                }

                Long totalAmount = returnInstallmentRepository.getAmountSumByLoanIdAndPeriod(loanId, period);

                if (totalAmount == null) totalAmount = 0L;
                payment.setTotalAmount(totalAmount);

                ReturnInstallment returnInstallment =
                        returnInstallmentRepository.findOneByTransactionAndReturnPeriod(loan.getTransactions().get(0)
                                , period);
                if (returnInstallment == null) {
                    payment.setPaid(false);
                } else {
                    if (returnInstallment.getReturnStatus() == ReturnStatus.paid) {
                        payment.setPaid(true);
                    } else {
                        payment.setPaid(false);
                    }
                }
                paymentList.add(payment);
            }

            return responseTemplate.success(paymentList);
        } catch (Exception e) {
            e.printStackTrace();
            return responseTemplate.internalServerError(e);
        }
    }

    @Override
    public Map withdraw(String email, Long loanId) {
        try {
            Startup startup = startupRepository.getByUserEmail(email);
            Loan loan = loanRepository.getById(loanId);
            if (startup.getId() != loan.getStartup().getId()) return responseTemplate.notAllowed("Not loan owner");

            Long currentValue = transactionRepository.sumOfPaidTransactionByLoanId(loanId);
            if (currentValue == null) return responseTemplate.notAllowed("Nothing to withdraw");

            Date currentDate = new Date();
            Integer isEnded = currentDate.compareTo(loan.getEndDate());
            if ((currentValue < loan.getTargetValue()) && (isEnded <= 0)) {
                return responseTemplate.notAllowed("Loan period not ended and target value not achieved");
            }

            WithdrawalInvoice withdrawalInvoice = withdrawalInvoiceRepository.findByLoan(loan);
            if (withdrawalInvoice != null) return responseTemplate.notAllowed("Already withdrawn");

            withdrawalInvoice = new WithdrawalInvoice();
            withdrawalInvoice.setLoan(loan);
            withdrawalInvoice.setPaymentDate(new Date());
            withdrawalInvoice.setAmount(currentValue);

            WithdrawalInvoice savedWithdrawalInvoice = withdrawalInvoiceRepository.save(withdrawalInvoice);
            loan.setWithdrawn(true);


            return responseTemplate.success(savedWithdrawalInvoice);

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
