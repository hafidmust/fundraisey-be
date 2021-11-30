package com.fundraisey.backend.controller.startup;

import com.fundraisey.backend.model.startup.CredentialModel;
import com.fundraisey.backend.service.UserService;
import com.fundraisey.backend.service.startup.CredentialService;
import com.fundraisey.backend.service.startup.ProductService;
import com.fundraisey.backend.util.ResponseTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/v1/startup")
public class CredentialController {
    @Autowired
    UserService userService;

    @Autowired
    CredentialService credentialService;

    ResponseTemplate responseTemplate = new ResponseTemplate();

    @Secured("ROLE_STARTUP")
    @PostMapping("/credential/add")
    ResponseEntity<Map> insert(@Valid @RequestBody CredentialModel credentialModel, Principal principal) {
        Long id = (principal == null) ? null : userService.getUserById(principal);

        Map response = credentialService.insert(credentialModel, id);

        return responseTemplate.controllerHttpRestResponse(response);
    }

    @Secured("ROLE_STARTUP")
    @PostMapping("/credential/{id}/upload")
    ResponseEntity<Map> uploadFileCredential () {
        return null;
    }
}
