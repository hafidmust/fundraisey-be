package com.fundraisey.backend.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class StartupPaymentListModel {
    Integer period;
    @JsonFormat(pattern = "dd-MM-yyyy")
    Date returnDate;
    Long totalAmount;
    boolean isPaid;
}
