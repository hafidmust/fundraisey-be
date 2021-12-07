package com.fundraisey.backend.model;

import lombok.Data;

@Data
public class PortofolioSummaryModel {
    Long totalWithdrawn;
    Long totalWithdrawnThisMonth;
    Long totalFunding;
    Long totalReturn;
    Long upcomingReturn;
    Integer loanTransactionCount;
    Long balance;
}
