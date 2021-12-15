package com.fundraisey.backend.controller.startup;

import com.fundraisey.backend.util.ResponseTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/v1/startup")
public class NotificationController {
    ResponseTemplate responseTemplate = new ResponseTemplate();

    @Secured("ROLE_STARTUP")
    @GetMapping("/notification")
    ResponseEntity<Map> getNotificationById() {
        return null;
    }
}
