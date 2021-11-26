package com.fundraisey.backend.service.interfaces.investor;

import java.util.Map;

public interface ReturnInstallmentService {
    Map getPortofolio(String email);
    Map withdraw(Long returnInstallmentId);
}
