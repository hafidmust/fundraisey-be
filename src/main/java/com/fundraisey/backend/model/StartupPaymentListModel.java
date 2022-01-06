package com.fundraisey.backend.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class StartupPaymentListModel {
    Integer period;
    @JsonFormat(pattern = "yyyy-MM-dd")
    Date returnDate;
    Long totalAmount;
    Float interestRate;
    boolean isPaid;
    Long platformFee;
    Float platformFeeRate;
}
