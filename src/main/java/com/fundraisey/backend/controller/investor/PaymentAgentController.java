package com.fundraisey.backend.controller.investor;

import com.fundraisey.backend.service.interfaces.investor.PaymentAgentService;
import com.fundraisey.backend.util.ResponseTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/v1/payment-agent")
public class PaymentAgentController {
    @Autowired
    PaymentAgentService paymentAgentService;

    @Autowired
    ResponseTemplate responseTemplate;

    @GetMapping
    ResponseEntity<Map> getAll() {
        Map response = paymentAgentService.getAll();
        return responseTemplate.controllerHttpRestResponse(response);
    }
}
