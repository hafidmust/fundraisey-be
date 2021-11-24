package com.fundraisey.backend.controller.investor;

import com.fundraisey.backend.model.InvestorModel;
import com.fundraisey.backend.service.interfaces.investor.InvestorService;
import com.fundraisey.backend.util.ResponseTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/v1/user/investor")
public class InvestorController {
    @Autowired
    ResponseTemplate responseTemplate;
    @Autowired
    InvestorService investorService;

    @GetMapping("/detail")
    ResponseEntity<Map> getById(Principal principal) {
        Map response = investorService.getByEmail(principal.getName());

        return responseTemplate.controllerHttpRestResponse(response);
    }

    @PutMapping("/update")
    ResponseEntity<Map> update(@RequestBody InvestorModel investorModel, Principal principal) {
        investorModel.setEmail(principal.getName());
        Map response = investorService.updateWithEmail(investorModel);

        return responseTemplate.controllerHttpRestResponse(response);
    }
}
