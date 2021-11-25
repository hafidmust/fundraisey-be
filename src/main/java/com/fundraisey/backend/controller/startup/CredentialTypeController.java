package com.fundraisey.backend.controller.startup;

import com.fundraisey.backend.entity.startup.CredentialType;
import com.fundraisey.backend.service.startup.CredentialTypeService;
import com.fundraisey.backend.util.ResponseTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/v1/admin/credential-type")
public class CredentialTypeController {
    @Autowired
    CredentialTypeService credentialTypeService;

    ResponseTemplate responseTemplate = new ResponseTemplate();

    @GetMapping("/all")
    public ResponseEntity<Map> getAll() {
        Map response = credentialTypeService.getAll();
        return responseTemplate.controllerHttpRestResponse(response);
    }


    @Secured("ROLE_ADMIN")
    @PostMapping("/add")
    ResponseEntity<Map> insert(@Valid @RequestBody CredentialType credentialType) {
        Map response = credentialTypeService.insert(credentialType);
        return responseTemplate.controllerHttpRestResponse(response);
    }

    @Secured("ROLE_ADMIN")
    @PutMapping("/update")
    ResponseEntity<Map> update(@Valid @RequestBody CredentialType credentialType) {
        Map response = credentialTypeService.update(credentialType);
        return responseTemplate.controllerHttpRestResponse(response);
    }

    @Secured({"ROLE_ADMIN"})
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map> delete(@PathVariable(value = "id") Long id) {
        Map response = credentialTypeService.delete(id);
        return responseTemplate.controllerHttpRestResponse(response);
    }
}
