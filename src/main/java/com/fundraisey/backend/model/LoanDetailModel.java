package com.fundraisey.backend.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fundraisey.backend.entity.startup.PaymentPlan;
import com.fundraisey.backend.entity.startup.Startup;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class LoanDetailModel {
    Long id;
    String name;
    String description;
    @JsonFormat(pattern = "yyyy-MM-dd")
    Date startDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    Date endDate;
    Long targetValue;
    Long currentValue;
    Float interestRate;
    Integer lenderCount;
    String status;
    boolean isWithdrawn;
    PaymentPlan paymentPlan;
    List<StartupPaymentListModel> paymentList;
    Startup startup;
}
