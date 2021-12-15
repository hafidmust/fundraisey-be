package com.fundraisey.backend.model;

import lombok.Data;

@Data
public class StartupWithdrawRequestModel {
    Long loanId;
    String accountNumber;
    Long bankId;
}
