package com.fundraisey.backend.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class LoanModel {
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
    List<StartupPaymentListModel> paymentList;
}
