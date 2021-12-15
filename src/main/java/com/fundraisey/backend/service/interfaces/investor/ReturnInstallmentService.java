package com.fundraisey.backend.service.interfaces.investor;

import java.util.Map;

public interface ReturnInstallmentService {
    Map getPortofolio(String email);
    Map getPortofolioSummary(String email);
    Map withdraw(String email, Long returnInstallmentId);
    Map withdrawAll(String email);
}
