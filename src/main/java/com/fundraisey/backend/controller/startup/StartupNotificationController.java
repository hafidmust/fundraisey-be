package com.fundraisey.backend.controller.startup;

import com.fundraisey.backend.service.UserService;
import com.fundraisey.backend.service.startup.StartupNotificationService;
import com.fundraisey.backend.util.ResponseTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/v1/startup/notification")
public class StartupNotificationController {
    @Autowired
    UserService userService;

    @Autowired
    StartupNotificationService startupNotificationService;

    ResponseTemplate responseTemplate = new ResponseTemplate();

    @Secured("ROLE_STARTUP")
    @GetMapping("/all")
    ResponseEntity<Map> getAll (
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size,
            @RequestParam(value = "sort-by", required = false) String sortAttribute,
            @RequestParam(value = "sort-type", required = false) String sortType,
            @RequestParam(required = false) Long userId,
            Principal principal
    ) {
        page = (page == null) ? 0 : page;
        size = (size == null) ? 20 : size;
        sortAttribute = (sortAttribute == null) ? "" : sortAttribute;
        sortType = (sortType == null) ? "" : sortType;

        Map response = startupNotificationService.getByUserId(page, size, sortAttribute, sortType, principal);

        return responseTemplate.controllerHttpRestResponse(response);
    }
}
