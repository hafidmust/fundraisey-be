package com.fundraisey.backend.model;

import com.fundraisey.backend.entity.startup.PaymentPlan;
import com.fundraisey.backend.entity.transaction.ReturnStatus;
import lombok.Data;

import java.util.Date;

@Data
public class PaymentDetailResponseModel {
    Integer returnPeriod;
    Date returnDate;
    ReturnStatus status;
    Long loanId;
    String loanName;
    Long amountPeriod;
    Long totalPaymentAmount;
    Long totalFundRaised;
    Long interest;
    PaymentPlan paymentPlan;
}
