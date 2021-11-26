package com.fundraisey.backend.controller.startup;

import com.fundraisey.backend.model.StartupLoanRequestModel;
import com.fundraisey.backend.service.interfaces.startup.LoanService;
import com.fundraisey.backend.util.ResponseTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/v1/startup/loan")
public class StartupLoanController {
    @Autowired
    LoanService loanService;
    @Autowired
    ResponseTemplate responseTemplate;

    @GetMapping("/{id}/payment-list")
    ResponseEntity<Map> getPaymentList(@PathVariable("id") Long loanId) {
        Map response = loanService.getPaymentList(loanId);

        return responseTemplate.controllerHttpRestResponse(response);
    }

    @PostMapping("/pay")
    ResponseEntity<Map> pay(@RequestBody StartupLoanRequestModel startupLoanRequestModel, Principal principal) {
        Map response = loanService.pay(principal.getName(), startupLoanRequestModel.getLoanId(),
                startupLoanRequestModel.getPeriod());

        return responseTemplate.controllerHttpRestResponse(response);
    }

    @PostMapping("/withdraw")
    ResponseEntity<Map> withdraw(@RequestBody StartupLoanRequestModel startupLoanRequestModel, Principal principal) {
        Map response = loanService.withdraw(principal.getName(), startupLoanRequestModel.getLoanId());

        return responseTemplate.controllerHttpRestResponse(response);
    }
}
