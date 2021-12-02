package com.fundraisey.backend.controller.startup;

import com.fundraisey.backend.service.interfaces.startup.HomeService;
import com.fundraisey.backend.util.ResponseTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/v1/startup")
public class HomeController {
    @Autowired
    HomeService homeService;

    ResponseTemplate responseTemplate = new ResponseTemplate();

    @Secured("ROLE_STARTUP")
    @GetMapping("/index")
    ResponseEntity<Map> getStartupById(Principal principal) {
        Map response = homeService.getIndexData(principal.getName());

        return responseTemplate.controllerHttpRestResponse(response);
    }
}
