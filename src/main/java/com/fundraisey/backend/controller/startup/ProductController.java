package com.fundraisey.backend.controller.startup;

import com.fundraisey.backend.model.startup.ProductModel;
import com.fundraisey.backend.service.interfaces.UserService;
import com.fundraisey.backend.service.interfaces.startup.ProductService;
import com.fundraisey.backend.util.ResponseTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/v1/startup")
public class ProductController {
    @Autowired
    UserService userService;

    @Autowired
    ProductService productService;

    ResponseTemplate responseTemplate = new ResponseTemplate();

    @Secured("ROLE_STARTUP")
    @PostMapping("/products/add")
    ResponseEntity<Map> insert(@Valid @RequestBody ProductModel productModel, Principal principal) {
        Long id = (principal == null) ? null : userService.getUserById(principal);

        Map response = productService.insert(productModel, id);

        return responseTemplate.controllerHttpRestResponse(response);

    }

    @Secured("ROLE_STARTUP")
    @PostMapping("/products/{id}/upload")
    ResponseEntity<Map> uploadFileProduct () {
        return null;
    }
}
