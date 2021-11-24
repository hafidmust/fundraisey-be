package com.fundraisey.backend.service.interfaces.auth;

import com.fundraisey.backend.model.LoginModel;

import java.util.Map;

public interface LoginService {
    Map login(LoginModel loginModel);
}
