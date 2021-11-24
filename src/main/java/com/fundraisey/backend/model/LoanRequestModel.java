package com.fundraisey.backend.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class LoanRequestModel {
    String title;
    Long targetValue;
    String description;
    @JsonFormat(pattern="dd-MM-yyyy")
    Date endDate;
    Float interestRate;
    Long paymentPlanId;
}
