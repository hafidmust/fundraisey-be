package com.fundraisey.backend.service.implementation.startup;

import com.fundraisey.backend.entity.auth.User;
import com.fundraisey.backend.entity.startup.Startup;
import com.fundraisey.backend.model.HomeResponseModel;
import com.fundraisey.backend.repository.auth.UserRepository;
import com.fundraisey.backend.repository.investor.TransactionRepository;
import com.fundraisey.backend.repository.startup.HomeRepository;
import com.fundraisey.backend.repository.startup.LoanRepository;
import com.fundraisey.backend.repository.startup.StartupRepository;
import com.fundraisey.backend.service.interfaces.startup.HomeService;
import com.fundraisey.backend.util.ResponseTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class HomeImplementation implements HomeService {
    @Autowired
    StartupRepository startupRepository;
    @Autowired
    LoanRepository loanRepository;
    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    UserRepository userRepository;

    ResponseTemplate responseTemplate = new ResponseTemplate();

    @Override
    public Map getIndexData(String email) {
        try {
            Startup startup = startupRepository.getByUserEmail(email);
            Long amountRaised = transactionRepository.sumOfPaidTransactionByStartupId(startup.getId());
            if (amountRaised == null) amountRaised = 0L;
            Long amountWithdrawAvailable = transactionRepository.sumOfAmountWithdrawnAvailableByStartupId(startup.getId());
            if (amountWithdrawAvailable == null) amountWithdrawAvailable = 0L;

            HomeResponseModel homeResponseModel = new HomeResponseModel();
            homeResponseModel.setStartupName(startup.getName());
            homeResponseModel.setAmountRaised(amountRaised);
            homeResponseModel.setAmountWithdrawAvailable(amountWithdrawAvailable);

            return responseTemplate.success(homeResponseModel);
        } catch (Exception e) {
            e.printStackTrace();
            return responseTemplate.internalServerError(e);
        }
    }
}
