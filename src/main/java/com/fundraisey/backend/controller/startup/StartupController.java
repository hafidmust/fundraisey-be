package com.fundraisey.backend.controller.startup;

import com.fundraisey.backend.model.startup.StartupModel;
import com.fundraisey.backend.service.interfaces.UserService;
import com.fundraisey.backend.service.interfaces.startup.StartupService;
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

    @Secured({"ROLE_STARTUP", "ROLE_ADMIN"})
    @GetMapping("/startup")
    ResponseEntity<Map> getAll(
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

        Map response = startupService.getByUserId(page, size, sortAttribute, sortType, principal);

        return responseTemplate.controllerHttpRestResponse(response);
    }

    @Secured("ROLE_STARTUP")
    @PostMapping(value = "/startup")
    ResponseEntity<Map> insert(@Valid @RequestBody StartupModel startupModel, Principal principal) {
        Long id = (principal == null) ? null : userService.getUserById(principal);
        Map response = startupService.insert(startupModel, id);
        return responseTemplate.controllerHttpRestResponse(response);
    }

    @Secured("ROLE_STARTUP")
    @PutMapping("/startup/update")
    ResponseEntity<Map> update(@Valid @RequestBody StartupModel startupModel, Principal principal) {
        Long id = (principal == null) ? null : userService.getUserById(principal);
        Map response = startupService.update(startupModel, id);
        return responseTemplate.controllerHttpRestResponse(response);
    }

    @Secured("ROLE_STARTUP")
    @GetMapping("/startup/detail")
    ResponseEntity<Map> getStartupProfile(Principal principal) {
        Long id = (principal == null) ? null : userService.getUserById(principal);
        Map response = startupService.getStartupById(id);
        return responseTemplate.controllerHttpRestResponse(response);
    }

    @Secured({"ROLE_STARTUP"})
    @DeleteMapping("/startup/delete/{id}")
    public ResponseEntity<Map> delete(@PathVariable(value = "id") Long id, Principal principal) {
        Long userId = (principal == null) ? null : userService.getUserById(principal);
        Map response = startupService.delete(id, userId);
        return responseTemplate.controllerHttpRestResponse(response);
    }

}
