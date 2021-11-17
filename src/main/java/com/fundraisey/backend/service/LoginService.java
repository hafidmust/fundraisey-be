package com.fundraisey.backend.service;

import com.fundraisey.backend.model.LoginModel;

import java.util.Map;

public interface LoginService {
    Map login(LoginModel loginModel);
}
