package com.fundraisey.backend.controller.startup;

import com.fundraisey.backend.model.startup.ProductModel;
import com.fundraisey.backend.model.startup.StartupModel;
import com.fundraisey.backend.service.UserService;
import com.fundraisey.backend.service.startup.StartupService;
import com.fundraisey.backend.util.ResponseTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/v1/users")
public class StartupController {
    @Autowired
    UserService userService;

    @Autowired
    StartupService startupService;

    ResponseTemplate responseTemplate = new ResponseTemplate();

    @Secured("ROLE_STARTUP")
    @GetMapping("/startup")
    ResponseEntity<Map> getStartup() { return null; }

    @Secured("ROLE_STARTUP")
    @PostMapping("/startup")
    ResponseEntity<Map> insertStartup(@Valid @RequestBody StartupModel startupModel, Principal principal) {
        Long id = (principal == null) ? null : userService.getUserById(principal);
        Map response = startupService.insert(startupModel, id);
        return responseTemplate.controllerHttpRestResponse(response);
    }

    @Secured("ROLE_STARTUP")
    @PutMapping("/startup/update")
    ResponseEntity<Map> updateStartup() {
        return null;
    }

    @Secured("ROLE_STARTUP")
    @GetMapping("/startup/detail")
    ResponseEntity<Map> getStartupProfile(Principal principal) {
        Long id = (principal == null) ? null : userService.getUserById(principal);
        Map response = startupService.getStartupById(id);
        return responseTemplate.controllerHttpRestResponse(response);
    }
}
