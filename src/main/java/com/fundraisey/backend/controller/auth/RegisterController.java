package com.fundraisey.backend.controller.auth;

import com.fundraisey.backend.model.RegisterModel;
import com.fundraisey.backend.repository.auth.UserRepository;
import com.fundraisey.backend.service.interfaces.auth.RegisterService;
import com.fundraisey.backend.util.ResponseTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1")
public class RegisterController {
    @Autowired
    RegisterService registerService;

    @Autowired
    UserRepository userRepository;

    ResponseTemplate responseTemplate = new ResponseTemplate();

    @PostMapping("/register-admin")
    public ResponseEntity<Map> registerAdmin(@RequestBody RegisterModel registerModel) {
        Map response = registerService.registerAdmin(registerModel);
        return responseTemplate.controllerHttpRestResponse(response);
    }

    @PostMapping("/register-startup")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Map> registerStartup(@RequestBody RegisterModel registerModel) {
        Map response = registerService.registerStartup(registerModel);
        return responseTemplate.controllerHttpRestResponse(response);
    }

    @PostMapping("/register-investor")
    public ResponseEntity<Map> registerInvestor(@RequestBody RegisterModel registerModel) {
        Map response = registerService.registerInvestor(registerModel);
        if ((Integer) response.get("status") == 200) {
            sendEmailRegister(registerModel);
        }
        return responseTemplate.controllerHttpRestResponse(response);
    }

    @PostMapping("send-email")
    public ResponseEntity<Map> sendEmailRegister(@RequestBody RegisterModel registerModel) {
        Map response = registerService.sendEmailRegister(registerModel.getEmail());
        return responseTemplate.controllerHttpRestResponse(response);
    }

    @GetMapping(value = "/user-register-confirm/{tokenotp}")
    public ResponseEntity<Map> confirmRegisterOTP(@PathVariable String tokenotp) {
        Map response = registerService.confirmRegisterOTP(tokenotp);
        return responseTemplate.controllerHttpRestResponse(response);
    }
}
