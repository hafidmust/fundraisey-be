package com.fundraisey.backend.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fundraisey.backend.entity.startup.Startup;
import lombok.Data;

import java.util.Date;

@Data
public class LoanDetailModel {
    Long id;
    String name;
    String description;
    @JsonFormat(pattern = "dd-MM-yyyy")
    Date startDate;
    @JsonFormat(pattern = "dd-MM-yyyy")
    Date endDate;
    Long targetValue;
    Long currentValue;
    Float interestRate;
    Startup startup;
}
