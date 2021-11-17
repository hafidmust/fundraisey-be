package com.fundraisey.backend.controller.auth;

import com.fundraisey.backend.model.LoginModel;
import com.fundraisey.backend.model.RegisterModel;
import com.fundraisey.backend.service.LoginService;
import com.fundraisey.backend.service.implementation.LoginImplementation;
import com.fundraisey.backend.util.ResponseTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1")
public class LoginController {
    @Autowired
    private LoginService loginService;

    ResponseTemplate responseTemplate = new ResponseTemplate();

    Logger logger = LoggerFactory.getLogger(LoginController.class);

    @PostMapping("/login")
    public ResponseEntity<Map> login(@RequestBody LoginModel loginModel) {
        return responseTemplate.controllerHttpRestResponse(loginService.login(loginModel));
    }
}
