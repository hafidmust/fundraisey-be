package com.fundraisey.backend.service.implementation.startup;

import com.fundraisey.backend.entity.auth.User;
import com.fundraisey.backend.entity.investor.Investor;
import com.fundraisey.backend.entity.startup.*;
import com.fundraisey.backend.entity.transaction.ReturnInstallment;
import com.fundraisey.backend.entity.transaction.ReturnStatus;
import com.fundraisey.backend.model.*;
import com.fundraisey.backend.repository.auth.UserRepository;
import com.fundraisey.backend.repository.investor.InvestorRepository;
import com.fundraisey.backend.repository.investor.ReturnInstallmentRepository;
import com.fundraisey.backend.repository.investor.TransactionRepository;
import com.fundraisey.backend.repository.startup.*;
import com.fundraisey.backend.service.interfaces.startup.LoanService;
import com.fundraisey.backend.util.ResponseTemplate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.NumberUtils;

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
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    BankRepository bankRepository;

    public Map insert(String email, LoanRequestModel loanRequestModel) {
        try {
            // Validation
            if (loanRequestModel.getTitle() == null || loanRequestModel.getTitle().equals(""))
                return responseTemplate.isRequired("Title required");
            if (loanRequestModel.getTargetValue() == null || loanRequestModel.getTargetValue().equals(""))
                return responseTemplate.isRequired("Target value required");
            if (loanRequestModel.getEndDate() == null || loanRequestModel.getEndDate().equals(""))
                return responseTemplate.isRequired("End date required");
            if (loanRequestModel.getPaymentPlanId() == null)
                return responseTemplate.isRequired("Payment plan id required");

            User user = userRepository.findOneByEmail(email);
            Startup startup = startupRepository.findByUser(user);
            PaymentPlan paymentPlan = paymentPlanRepository.getById(loanRequestModel.getPaymentPlanId());

            // Create new loan
            Loan loan = new Loan();

            loan.setStartup(startup);
            loan.setName(loanRequestModel.getTitle());
            loan.setTargetValue(loanRequestModel.getTargetValue());
            loan.setDescription(loanRequestModel.getDescription());
            loan.setStartDate(new Date());
            loan.setEndDate(loanRequestModel.getEndDate());
            loan.setInterestRate(paymentPlan.getInterestRate());
            loan.setPaymentPlan(paymentPlan);
            loan.setStatus(LoanStatus.pending);

            // Add total return period
            if (paymentPlan == null) return responseTemplate.notFound("Payment plan not found");
            loan.setTotalReturnPeriod(paymentPlan.getTotalPeriod());

            Loan loanObj = loanRepository.save(loan);

            // Add payment list
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(loanObj.getEndDate());
            for (Integer period = 1; period <= loanObj.getTotalReturnPeriod(); period++) {
                Payment payment = new Payment();
                payment.setLoan(loanObj);
                payment.setReturnPeriod(period);
                payment.setStatus(ReturnStatus.unpaid);
                if (period == 1) {
                    calendar.add(Calendar.YEAR, 2);
                } else {
                    calendar.add(Calendar.MONTH, paymentPlan.getMonthInterval());
                }
                payment.setReturnDate(calendar.getTime());
                paymentRepository.save(payment);
            }

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
            List<LoanModel> loanList = new ArrayList<>();
            LoanResponseModel response = new LoanResponseModel();

            if ((sortType.equals("desc")) || (sortType.equals("descending"))) {
                pageable = PageRequest.of(page, size, Sort.by(sortAttribute).descending());
            } else {
                pageable = PageRequest.of(page, size, Sort.by(sortAttribute).ascending());
            }

            loans = loanRepository.findByStartupAndNameContainingIgnoreCase(startup, search,pageable);
            for (Loan loan : loans.getContent()) {
                LoanModel loanModel = createLoanModel(loan);

                loanList.add(loanModel);
            }
            response.setContent(loanList);
            response.setTotalElements(loans.getTotalElements());
            response.setTotalPages(loans.getTotalPages());
            response.setNumberOfElements(loans.getNumberOfElements());
            response.setSize(loans.getSize());
            response.setPageNumber(loans.getNumber());

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

            loans = loanRepository.getAcceptedAndUnfinishedLoanByNameContainingOrStartupNameContaining(search, pageable);
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
            if (loan == null) return responseTemplate.notFound("Loan not found");

            LoanDetailModel loanDetailModel = createLoanDetailModel(loan);

            return responseTemplate.success(loanDetailModel);
        } catch (Exception e) {
            e.printStackTrace();
            return responseTemplate.internalServerError(e);
        }
    }

    @Override
    public Map payInvestor(String email, Long loanId, Integer period) {
        Map<String, Object> response = new HashMap<>();
        try {
            Startup startup = startupRepository.getByUserEmail(email);
            Loan loan = loanRepository.getById(loanId);
            if (loan == null) return responseTemplate.notFound("Loan not found");
            if (startup.getId() != loan.getStartup().getId()) return responseTemplate.notAllowed("Not loan owner");
            Payment payment = paymentRepository.getByLoanIdAndPeriod(loanId, period);

            List<ReturnInstallment> returnInstallments =
                    returnInstallmentRepository.getByLoanIdAndPeriod(loanId, period);
            Integer paidCount = 0;
            Long paymentSum = 0L;
            for (ReturnInstallment returnInstallment : returnInstallments) {
                if (returnInstallment.getReturnStatus() == ReturnStatus.paid) {
                    continue;
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
                    paidCount++;
                    paymentSum = paymentSum + returnInstallment.getAmount();
                }
            }

            payment.setStatus(ReturnStatus.paid);
            Payment saved = paymentRepository.save(payment);

            response.put("paid transaction count", paidCount);
            response.put("unpaid transaction count", returnInstallments.size() - paidCount);
            response.put("payment sum", paymentSum);
            response.put("paymentData", payment);
            return responseTemplate.success(response);
        } catch (Exception e) {
            e.printStackTrace();
            return responseTemplate.internalServerError(e);
        }
    }

    @Override
    public Map getPaymentList(Long loanId) {
        try {
            List<StartupPaymentListModel> paymentList = createPaymentList(loanId);

            return responseTemplate.success(paymentList);
        } catch (Exception e) {
            e.printStackTrace();
            return responseTemplate.internalServerError(e);
        }
    }

    public Map getPaymentDetail(Long loanId, Integer period) {
        try {
            Payment payment = paymentRepository.getByLoanIdAndPeriod(loanId, period);

            return responseTemplate.success(payment);
        } catch (Exception e) {
            e.printStackTrace();
            return responseTemplate.internalServerError(e);
        }
    }

    private List<StartupPaymentListModel> createPaymentList(Long loanId) {
        Loan loan = loanRepository.getById(loanId);
        Integer totalReturnPeriod = loan.getTotalReturnPeriod();
        List<Payment> payments = paymentRepository.getByLoanId(loanId);
        List<StartupPaymentListModel> paymentListModel = new ArrayList<>();

        // Create payment date(s)
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(loan.getEndDate());
        for (Integer period = 1; period <= totalReturnPeriod; period++) {
            Payment payment = paymentRepository.getByLoanIdAndPeriod(loanId, period);
            StartupPaymentListModel paymentModel = new StartupPaymentListModel();
            paymentModel.setPeriod(period);
            paymentModel.setReturnDate(payment.getReturnDate());

            Long totalAmount = returnInstallmentRepository.getAmountSumByLoanIdAndPeriod(loanId, period);

            if (totalAmount == null) totalAmount = 0L;
            paymentModel.setTotalAmount(totalAmount);

            if (loan.getTransactions().size() != 0) {
                ReturnInstallment returnInstallment =
                        returnInstallmentRepository.findOneByTransactionAndPayment(loan.getTransactions().get(0)
                                , payment);

                if (returnInstallment == null) {
                    paymentModel.setPaid(false);
                } else {
                    if (returnInstallment.getReturnStatus() == ReturnStatus.paid) {
                        paymentModel.setPaid(true);
                    } else {
                        paymentModel.setPaid(false);
                    }
                }
            } else {
                paymentModel.setPaid(false);
            }

            paymentListModel.add(paymentModel);
        }

        return paymentListModel;
    }

    @Override
    public Map withdraw(String email, StartupWithdrawRequestModel withdrawRequestModel) {
        try {
            if (withdrawRequestModel.getLoanId() == null) return responseTemplate.isRequired("Please insert loan id");
            if (withdrawRequestModel.getBankId() == null) return responseTemplate.isRequired("Please insert bank id");
            if (withdrawRequestModel.getAccountNumber() == null) return responseTemplate.isRequired("Please insert " +
                    "account number");
            if (!StringUtils.isNumericSpace(withdrawRequestModel.getAccountNumber()))
                return responseTemplate.notAllowed("Account value is not numeric");

            Bank bank = bankRepository.getById(withdrawRequestModel.getBankId());
            if (bank == null) return responseTemplate.notFound("Bank not found");

            Startup startup = startupRepository.getByUserEmail(email);
            Loan loan = loanRepository.getById(withdrawRequestModel.getLoanId());
            if (startup.getId() != loan.getStartup().getId()) return responseTemplate.notAllowed("Not loan owner");

            Long currentValue = transactionRepository.sumOfPaidTransactionByLoanId(withdrawRequestModel.getLoanId());
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
            withdrawalInvoice.setBank(bank);
            withdrawalInvoice.setAccountNumber(withdrawalInvoice.getAccountNumber());

            WithdrawalInvoice savedWithdrawalInvoice = withdrawalInvoiceRepository.save(withdrawalInvoice);
            loan.setWithdrawn(true);

            return responseTemplate.success(savedWithdrawalInvoice);

        } catch (Exception e) {
            e.printStackTrace();
            return responseTemplate.internalServerError(e);
        }
    }

    @Override
    public Map getWithdrawalHistory(String email) {
        try {
            Startup startup = startupRepository.getByUserEmail(email);
            List<WithdrawalInvoice> withdrawals = withdrawalInvoiceRepository.getByStartupId(startup.getId());

            return responseTemplate.success(withdrawals);
        } catch (Exception e) {
            e.printStackTrace();
            return responseTemplate.internalServerError(e);
        }
    }

    @Override
    public Map getBankList() {
        try {
            List<Bank> banks = bankRepository.findAll();;
            if (banks == null) return responseTemplate.notFound("Not found");

            return responseTemplate.success(banks);
        } catch (Exception e) {
            e.printStackTrace();
            return responseTemplate.internalServerError(e);
        }
    }

    private LoanDetailModel createLoanDetailModel(Loan loan) {
        Long currentValue = transactionRepository.sumOfPaidTransactionByLoanId(loan.getId());
        if (currentValue == null) currentValue = 0L;
        Integer lenderCount = transactionRepository.countOfInvestorByLoanId(loan.getId());
        if (lenderCount == null) lenderCount = 0;

        LoanDetailModel loanDetailModel = new LoanDetailModel();
        loanDetailModel.setId(loan.getId());
        loanDetailModel.setName(loan.getName());
        loanDetailModel.setDescription(loan.getDescription());
        loanDetailModel.setStartDate(loan.getStartDate());
        loanDetailModel.setEndDate(loan.getEndDate());
        loanDetailModel.setTargetValue(loan.getTargetValue());
        loanDetailModel.setCurrentValue(currentValue);
        loanDetailModel.setInterestRate(loan.getInterestRate());
        loanDetailModel.setStatus(loan.getStatus().toString());
        loanDetailModel.setLenderCount(lenderCount);
        loanDetailModel.setPaymentList(createPaymentList(loan.getId()));
        loanDetailModel.setStartup(loan.getStartup());
        loanDetailModel.setWithdrawn(loan.isWithdrawn());

        return loanDetailModel;
    }

    private LoanModel createLoanModel(Loan loan) {
        Long currentValue = transactionRepository.sumOfPaidTransactionByLoanId(loan.getId());
        if (currentValue == null) currentValue = 0L;
        Integer lenderCount = transactionRepository.countOfInvestorByLoanId(loan.getId());
        if (lenderCount == null) lenderCount = 0;

        LoanModel loanModel = new LoanModel();
        loanModel.setId(loan.getId());
        loanModel.setName(loan.getName());
        loanModel.setDescription(loan.getDescription());
        loanModel.setStartDate(loan.getStartDate());
        loanModel.setEndDate(loan.getEndDate());
        loanModel.setTargetValue(loan.getTargetValue());
        loanModel.setCurrentValue(currentValue);
        loanModel.setInterestRate(loan.getInterestRate());
        loanModel.setStatus(loan.getStatus().toString());
        loanModel.setLenderCount(lenderCount);
        loanModel.setPaymentList(createPaymentList(loan.getId()));
        loanModel.setWithdrawn(loan.isWithdrawn());

        return loanModel;
    }
}
