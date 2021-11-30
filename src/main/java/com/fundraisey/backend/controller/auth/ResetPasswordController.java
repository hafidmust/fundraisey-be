package com.fundraisey.backend.controller.auth;

import com.fundraisey.backend.model.ResetPasswordModel;
import com.fundraisey.backend.service.interfaces.auth.ResetPasswordService;
import com.fundraisey.backend.util.ResponseTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/v1/reset-password")
public class ResetPasswordController {
    @Autowired
    ResetPasswordService resetPasswordService;

    ResponseTemplate responseTemplate = new ResponseTemplate();

    @PostMapping("/send")
    public ResponseEntity<Map> sendEmailResetPassword(@RequestBody ResetPasswordModel resetPasswordModel) {
        Map response = resetPasswordService.sendEmailResetPassword(resetPasswordModel.getEmail());

        return responseTemplate.controllerHttpRestResponse(response);
    }

    @PostMapping("/reset")
    public ResponseEntity<Map> resetPassword(@RequestBody ResetPasswordModel resetPasswordModel) {
        Map response = resetPasswordService.resetPassword(resetPasswordModel.getOtp(),
                resetPasswordModel.getNewPassword());

        return responseTemplate.controllerHttpRestResponse(response);
    }
}
