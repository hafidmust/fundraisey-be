package com.fundraisey.backend.controller.startup;

import com.fundraisey.backend.model.LoanRequestModel;
import com.fundraisey.backend.service.interfaces.startup.LoanService;
import com.fundraisey.backend.util.ResponseTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/v1/loan")
public class LoanController {
    @Autowired
    LoanService loanService;
    @Autowired
    ResponseTemplate responseTemplate;

    @PostMapping("/insert")
    ResponseEntity<Map> insert(@RequestBody LoanRequestModel loanRequestModel, Principal principal) {
        Map response = loanService.insert(principal.getName(), loanRequestModel);

        return responseTemplate.controllerHttpRestResponse(response);
    }

    @GetMapping("/all")
    ResponseEntity<Map> getAllAccepted(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size,
            @RequestParam(value = "sort-by", required = false) String sortAttribute,
            @RequestParam(value = "sort-type", required = false) String sortType,
            @RequestParam(value = "search", required = false) String search
    ) {
        page = (page == null) ? 0 : page;
        size = (size == null) ? 20 : size;
        sortAttribute = (sortAttribute == null) ? "" : sortAttribute;
        sortType = (sortType == null) ? "" : sortType;
        search = (search == null) ? "" : search;

        Map response = loanService.getAllAccepted(page, size, sortAttribute, sortType, search);

        return responseTemplate.controllerHttpRestResponse(response);
    }

    @GetMapping("/detail/{id}")
    ResponseEntity<Map> getById(
            @PathVariable("id") Long id
    ) {
        Map response = loanService.getById(id);

        return responseTemplate.controllerHttpRestResponse(response);
    }

    @Secured("ROLE_STARTUP")
    @GetMapping("/startup")
    ResponseEntity<Map> getAllByStartupToken(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size,
            @RequestParam(value = "sort-by", required = false) String sortAttribute,
            @RequestParam(value = "sort-type", required = false) String sortType,
            @RequestParam(value = "search", required = false) String search,
            Principal principal
    ) {
        page = (page == null) ? 0 : page;
        size = (size == null) ? 20 : size;
        sortAttribute = (sortAttribute == null) ? "" : sortAttribute;
        sortType = (sortType == null) ? "" : sortType;
        search = (search == null) ? "" : search;

        Map response = loanService.getAllByEmail(page, size, sortAttribute, sortType, search, principal.getName());

        return responseTemplate.controllerHttpRestResponse(response);
    }
}
