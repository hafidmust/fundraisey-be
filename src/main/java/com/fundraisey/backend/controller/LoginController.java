package com.fundraisey.backend.controller;

import com.fundraisey.backend.model.LoginModel;
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

import java.util.Map;

@RestController
@RequestMapping("/v1")
public class LoginController {
    @Autowired
    private LoginService loginService;

    @Autowired
    private Environment env;

    Logger logger = LoggerFactory.getLogger(LoginImplementation.class);

    private ResponseTemplate responseTemplate = new ResponseTemplate();

    @PostMapping("/login")
    public ResponseEntity<Map> login(@RequestBody LoginModel loginModel) {
        return new ResponseEntity<Map>(loginService.login(loginModel), HttpStatus.OK);
    }

    @GetMapping("/env")
    public ResponseEntity<Map> getEnv() {
        return new ResponseEntity<Map>(responseTemplate.success(env.getActiveProfiles()), HttpStatus.OK);
    }
}
