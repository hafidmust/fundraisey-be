package com.fundraisey.backend.controller.startup;

import com.fundraisey.backend.entity.startup.SocialMediaPlatform;
import com.fundraisey.backend.service.startup.SocialMediaPlatformService;
import com.fundraisey.backend.util.ResponseTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/v1/admin/social-media-platform")
public class SocialMediaPlatformController {
    @Autowired
    SocialMediaPlatformService socialMediaPlatformService;

    ResponseTemplate responseTemplate = new ResponseTemplate();

    @GetMapping("/all")
    public ResponseEntity<Map> getAll() {
        Map response = socialMediaPlatformService.getAll();
        return responseTemplate.controllerHttpRestResponse(response);
    }


    @Secured("ROLE_ADMIN")
    @PostMapping("/add")
    ResponseEntity<Map> insert(@Valid @RequestBody SocialMediaPlatform socialMediaPlatform) {
        Map response = socialMediaPlatformService.insert(socialMediaPlatform);
        return responseTemplate.controllerHttpRestResponse(response);
    }

    @Secured("ROLE_ADMIN")
    @PutMapping("/update")
    ResponseEntity<Map> update(@Valid @RequestBody SocialMediaPlatform socialMediaPlatform) {
        Map response = socialMediaPlatformService.update(socialMediaPlatform);
        return responseTemplate.controllerHttpRestResponse(response);
    }

    @Secured({"ROLE_ADMIN"})
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map> delete(@PathVariable(value = "id") Long id) {
        Map response = socialMediaPlatformService.delete(id);
        return responseTemplate.controllerHttpRestResponse(response);
    }

}
