package com.fundraisey.backend.service.implementation.investor;

import com.fundraisey.backend.entity.transaction.PaymentAgent;
import com.fundraisey.backend.repository.investor.PaymentAgentRepository;
import com.fundraisey.backend.service.interfaces.investor.PaymentAgentService;
import com.fundraisey.backend.util.ResponseTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PaymentAgentImplementation implements PaymentAgentService {
    @Autowired
    PaymentAgentRepository paymentAgentRepository;

    @Autowired
    ResponseTemplate responseTemplate;

    @Override
    public Map getAll() {
        try {
            List<PaymentAgent> paymentAgentList = paymentAgentRepository.getAll();
            return responseTemplate.success(paymentAgentList);
        } catch (Exception e) {
            e.printStackTrace();
            return responseTemplate.internalServerError(e);
        }
    }
}
