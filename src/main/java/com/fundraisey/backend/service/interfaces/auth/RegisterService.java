package com.fundraisey.backend.service.interfaces.auth;

import com.fundraisey.backend.model.RegisterModel;

import java.util.Map;

public interface RegisterService {
    Map registerAdmin(RegisterModel registerModel);
    Map registerStartup(RegisterModel registerModel);
    Map registerInvestor(RegisterModel registerModel);
    Map sendEmailRegister(String email);
    Map confirmRegisterOTP(String otp);
}
