package com.fundraisey.backend.controller.startup;

import com.fundraisey.backend.model.CredentialRequestModel;
import com.fundraisey.backend.service.interfaces.startup.CredentialService;
import com.fundraisey.backend.util.ResponseTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/v1/startup")
public class CredentialController {
    @Autowired
    CredentialService credentialService;

    ResponseTemplate responseTemplate = new ResponseTemplate();

    @Secured("ROLE_STARTUP")
    @PostMapping("/credential/add")
    ResponseEntity<Map> insertCredential(@RequestBody CredentialRequestModel credentialRequestModel, Principal principal) {
        Map response = credentialService.insert(principal.getName(), credentialRequestModel);

        return responseTemplate.controllerHttpRestResponse(response);
    }

    @Secured("ROLE_STARTUP")
    @PostMapping("/credential/{id}/upload")
    ResponseEntity<Map> uploadFileCredential () {
        return null;
    }
}
