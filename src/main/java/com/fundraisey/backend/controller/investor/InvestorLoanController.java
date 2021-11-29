package com.fundraisey.backend.controller.investor;

import com.fundraisey.backend.model.InvestorLoanModel;
import com.fundraisey.backend.service.interfaces.investor.ReturnInstallmentService;
import com.fundraisey.backend.util.ResponseTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/v1/investor/loan")
public class InvestorLoanController {
    @Autowired
    ReturnInstallmentService returnInstallmentService;
    @Autowired
    ResponseTemplate responseTemplate;

    @GetMapping("/portofolio")
    ResponseEntity<Map> portofolio(Principal principal) {
        Map response = returnInstallmentService.getPortofolio(principal.getName());

        return responseTemplate.controllerHttpRestResponse(response);
    }
    @PostMapping("/withdraw")
    ResponseEntity<Map> withdraw(@RequestBody InvestorLoanModel investorLoanModel) {
        Map response = returnInstallmentService.withdraw(investorLoanModel.getReturnId());

        return responseTemplate.controllerHttpRestResponse(response);
    }
}
