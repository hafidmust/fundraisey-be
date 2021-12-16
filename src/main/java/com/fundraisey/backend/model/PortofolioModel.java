package com.fundraisey.backend.model;

import com.fundraisey.backend.entity.startup.Loan;
import com.fundraisey.backend.entity.startup.Startup;
import com.fundraisey.backend.entity.transaction.ReturnInstallment;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PortofolioModel {
    Long transactionId;
    Long startupId;
    String startupName;
    Long loanId;
    String loanName;
    Date startDate;
    Date endDate;
    Long currentLoanValue;
    Long targetLoanValue;
    Long paymentPlanId;
    String paymentPlanName;
    List<ReturnInstallment> returnInstallment;
}
