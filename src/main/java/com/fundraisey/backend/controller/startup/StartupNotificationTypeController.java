package com.fundraisey.backend.controller.startup;

import com.fundraisey.backend.entity.startup.StartupNotificationType;
import com.fundraisey.backend.service.startup.StartupNotificationTypeService;
import com.fundraisey.backend.util.ResponseTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/v1/admin/startup-notification-type")
public class StartupNotificationTypeController {
    @Autowired
    StartupNotificationTypeService startupNotificationTypeService;

    ResponseTemplate responseTemplate = new ResponseTemplate();

    @GetMapping
    public ResponseEntity<Map> getAll() {
        Map response = startupNotificationTypeService.getAll();
        return responseTemplate.controllerHttpRestResponse(response);
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/add")
    ResponseEntity<Map> insert(@Valid @RequestBody StartupNotificationType startupNotificationType) {
        Map response = startupNotificationTypeService.insert(startupNotificationType);
        return responseTemplate.controllerHttpRestResponse(response);
    }

    @Secured("ROLE_ADMIN")
    @PutMapping("/update")
    ResponseEntity<Map> update(@Valid @RequestBody StartupNotificationType startupNotificationType) {
        Map response = startupNotificationTypeService.update(startupNotificationType);
        return responseTemplate.controllerHttpRestResponse(response);
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map> delete(@PathVariable(value = "id") Long id) {
        Map response = startupNotificationTypeService.delete(id);

        return responseTemplate.controllerHttpRestResponse(response);
    }
}
