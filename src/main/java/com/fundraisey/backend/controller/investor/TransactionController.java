package com.fundraisey.backend.controller.investor;

import com.fundraisey.backend.model.TransactionRequestModel;
import com.fundraisey.backend.service.interfaces.investor.TransactionService;
import com.fundraisey.backend.util.ResponseTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/v1/investor/transaction")
public class TransactionController {
    @Autowired
    TransactionService transactionService;
    @Autowired
    ResponseTemplate responseTemplate;

    @PostMapping("/insert")
    ResponseEntity<Map> insert(@RequestBody TransactionRequestModel transactionRequestModel, Principal principal) {
        Map response = transactionService.insert(principal.getName(), transactionRequestModel);

        return responseTemplate.controllerHttpRestResponse(response);
    }

    @PostMapping("/pay")
    ResponseEntity<Map> pay(@RequestBody TransactionRequestModel transactionRequestModel, Principal principal) {
        Map response = transactionService.pay(principal.getName(), transactionRequestModel);

        return responseTemplate.controllerHttpRestResponse(response);
    }

    @PutMapping("/update")
    ResponseEntity<Map> update() {
        return null;
    }

    @Secured("ROLE_INVESTOR")
    @GetMapping("/all")
    ResponseEntity<Map> getAllByUserToken(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size,
            @RequestParam(value = "sort-by", required = false) String sortAttribute,
            @RequestParam(value = "sort-type", required = false) String sortType,
            Principal principal
    ) {
        page = (page == null) ? 0 : page;
        size = (size == null) ? 20 : size;
        sortAttribute = (sortAttribute == null) ? "" : sortAttribute;
        sortType = (sortType == null) ? "" : sortType;

        Map response = transactionService.getByEmail(page, size, sortAttribute, sortType, principal.getName());

        return responseTemplate.controllerHttpRestResponse(response);
    }

    @Secured("ROLE_INVESTOR")
    @GetMapping("/{id}")
    ResponseEntity<Map> getById(@PathVariable("id") Long id) {
        // Check if transaction is owned by the token owner?

        Map response = transactionService.getById(id);

        return responseTemplate.controllerHttpRestResponse(response);
    }

}
