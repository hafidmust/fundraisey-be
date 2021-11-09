package com.fundraisey.backend.service;

import java.util.Map;

public interface ResetPasswordService {
    Map sendEmailResetPassword(String email);
    Map resetPassword(String otp, String newPassword);
}
