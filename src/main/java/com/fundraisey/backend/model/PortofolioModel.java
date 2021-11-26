package com.fundraisey.backend.model;

import com.fundraisey.backend.entity.startup.Loan;
import com.fundraisey.backend.entity.startup.Startup;
import com.fundraisey.backend.entity.transaction.ReturnInstallment;
import lombok.Data;

@Data
public class PortofolioModel {
    ReturnInstallment returnInstallment;
    Loan loan;
}
