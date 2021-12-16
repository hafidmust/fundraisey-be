package com.fundraisey.backend.model;

import com.fundraisey.backend.entity.startup.Loan;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
public class HomeResponseModel {
    String startupName;
    Long amountRaised;
    Long amountWithdrawAvailable;
}
