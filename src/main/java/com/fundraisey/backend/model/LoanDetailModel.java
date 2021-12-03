package com.fundraisey.backend.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fundraisey.backend.entity.startup.Startup;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class LoanDetailModel {
    Long id;
    String name;
    String startupName;
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
    List<StartupPaymentListModel> paymentList;
}
