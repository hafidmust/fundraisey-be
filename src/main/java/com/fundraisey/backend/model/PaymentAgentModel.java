package com.fundraisey.backend.model;

import lombok.Data;

@Data
public class PaymentAgentModel {
    Long id;
    String transactionMethod;
    String name;
}
