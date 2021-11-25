package com.fundraisey.backend.controller.startup;

import com.fundraisey.backend.util.ResponseTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/v1/startup")
public class CredentialController {
    ResponseTemplate responseTemplate = new ResponseTemplate();

    @Secured("ROLE_STARTUP")
    @PostMapping("/credential/add")
    ResponseEntity<Map> insertLoan() {
        return null;
    }

    @Secured("ROLE_STARTUP")
    @PostMapping("/credential/{id}/upload")
    ResponseEntity<Map> uploadFileCredential () {
        return null;
    }
}
