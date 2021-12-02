package com.fundraisey.backend.service.interfaces.auth;

import java.util.Map;

public interface ResetPasswordService {
    Map sendEmailResetPassword(String email);
    Map resetPassword(String otp, String newPassword);
}
