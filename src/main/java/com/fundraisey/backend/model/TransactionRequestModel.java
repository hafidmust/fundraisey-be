package com.fundraisey.backend.model;

import lombok.Data;

@Data
public class TransactionRequestModel {
    Long loanId;
    Long amount;
    Long paymentAgentId;
    Long transactionId;
}
