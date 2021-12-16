package com.fundraisey.backend.controller;

import com.fundraisey.backend.model.UserModel;
import com.fundraisey.backend.service.interfaces.UserService;
import com.fundraisey.backend.util.ResponseTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/v1/user")
public class UserController {

    @Autowired
    UserService userService;

    ResponseTemplate responseTemplate = new ResponseTemplate();

    @Secured("ROLE_ADMIN")
    @GetMapping("/all")
    ResponseEntity<Map> getStartupList(
            @RequestParam(value = "role", required = false) String role,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size,
            @RequestParam(value = "sort-by", required = false) String sortAttribute,
            @RequestParam(value = "sort-type", required = false) String sortType
    ) {
        page = (page == null) ? 0 : page;
        size = (size == null) ? 20 : size;
        sortAttribute = (sortAttribute == null) ? "" : sortAttribute;
        sortType = (sortType == null) ? "" : sortType;

        Map response;

        if (role.equals("startup")) {
            response = userService.getAllByRole(page, size, sortAttribute, sortType, "ROLE_STARTUP");
        } else if (role.equals("investor")) {
            response = userService.getAllByRole(page, size, sortAttribute, sortType, "ROLE_INVESTOR");
        } else {
            response = responseTemplate.notFound("Role not found");
        }

        return responseTemplate.controllerHttpRestResponse(response);
    }

    @GetMapping("/detail")
    ResponseEntity<Map> getById(Principal principal) {
        Map response = userService.getByEmail(principal.getName());

        return responseTemplate.controllerHttpRestResponse(response);
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/detail/{id}")
    ResponseEntity<Map> getById(@PathVariable Long id) {
        Map response = userService.getById(id);

        return responseTemplate.controllerHttpRestResponse(response);
    }

    @PutMapping("/update")
    ResponseEntity<Map> updateByPrincipal(@RequestBody UserModel userModel, Principal principal) {
        userModel.setEmail(principal.getName());
        Map response = userService.updateWithEmail(userModel);

        return responseTemplate.controllerHttpRestResponse(response);
    }

    @Secured("ROLE_ADMIN")
    @PutMapping("/update/{id}")
    ResponseEntity<Map> updateById(@PathVariable Long id,@RequestBody UserModel userModel) {
        userModel.setId(id);
        Map response = userService.update(userModel);

        return responseTemplate.controllerHttpRestResponse(response);
    }
}
