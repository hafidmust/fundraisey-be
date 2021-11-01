package com.fundraisey.backend.controller;

import com.fundraisey.backend.model.LoginModel;
import com.fundraisey.backend.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/v1")
public class LoginController {
    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<Map> login(@RequestBody LoginModel loginModel) {
        return new ResponseEntity<Map>(loginService.login(loginModel), HttpStatus.OK);
    }
}
