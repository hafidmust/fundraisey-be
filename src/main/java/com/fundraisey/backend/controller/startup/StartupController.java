package com.fundraisey.backend.controller.startup;

import com.fundraisey.backend.model.StartupPICModel;
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
@RequestMapping("/v1/user/startup")
public class StartupController {
    @Autowired
    UserService userService;

    @Autowired
    StartupService startupService;

    ResponseTemplate responseTemplate = new ResponseTemplate();

    @Secured("ROLE_ADMIN")
    @GetMapping("/all")
    ResponseEntity<Map> getAll(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size,
            @RequestParam(value = "sort-by", required = false) String sortAttribute,
            @RequestParam(value = "sort-type", required = false) String sortType
    ) {
        page = (page == null) ? 0 : page;
        size = (size == null) ? 20 : size;
        sortAttribute = (sortAttribute == null) ? "" : sortAttribute;
        sortType = (sortType == null) ? "" : sortType;

        Map response = startupService.getAll(page, size, sortAttribute, sortType);

        return responseTemplate.controllerHttpRestResponse(response);
    }

    @Secured("ROLE_STARTUP")
    @PostMapping(value = "/create")
    ResponseEntity<Map> insert(@Valid @RequestBody StartupModel startupModel, Principal principal) {
        Long id = (principal == null) ? null : userService.getUserById(principal);
        Map response = startupService.insert(startupModel, principal.getName());
        return responseTemplate.controllerHttpRestResponse(response);
    }

    @Secured("ROLE_STARTUP")
    @PutMapping("/update")
    ResponseEntity<Map> update(@RequestBody StartupModel startupModel, Principal principal) {
        Long id = (principal == null) ? null : userService.getUserById(principal);
        Map response = startupService.update(startupModel, principal.getName());
        return responseTemplate.controllerHttpRestResponse(response);
    }

    @Secured("ROLE_STARTUP")
    @GetMapping("/detail")
    ResponseEntity<Map> getStartupProfile(Principal principal) {
        Long id = (principal == null) ? null : userService.getUserById(principal);
        Map response = startupService.getStartupById(id);
        return responseTemplate.controllerHttpRestResponse(response);
    }

    @Secured("ROLE_STARTUP")
    @GetMapping("/pic")
    ResponseEntity<Map> getPIC(Principal principal) {
        Map response = startupService.getPIC(principal.getName());
        return responseTemplate.controllerHttpRestResponse(response);
    }

    @Secured("ROLE_STARTUP")
    @PutMapping("/pic")
    ResponseEntity<Map> updatePIC(@RequestBody StartupPICModel startupPICModel, Principal principal) {
        Map response = startupService.updatePIC(principal.getName(), startupPICModel);
        return responseTemplate.controllerHttpRestResponse(response);
    }

//    @Secured({"ROLE_STARTUP"})
//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<Map> delete(@PathVariable(value = "id") Long id, Principal principal) {
//        Long userId = (principal == null) ? null : userService.getUserById(principal);
//        Map response = startupService.delete(id, userId);
//        return responseTemplate.controllerHttpRestResponse(response);
//    }

}
