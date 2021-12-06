package com.fundraisey.backend.service.implementation.investor;

import com.fundraisey.backend.entity.auth.User;
import com.fundraisey.backend.entity.investor.Investor;
import com.fundraisey.backend.model.InvestorModel;
import com.fundraisey.backend.model.UserModel;
import com.fundraisey.backend.repository.investor.InvestorRepository;
import com.fundraisey.backend.repository.auth.UserRepository;
import com.fundraisey.backend.service.interfaces.investor.InvestorService;
import com.fundraisey.backend.util.ResponseTemplate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class InvestorImplementation implements InvestorService {

    @Autowired
    ResponseTemplate responseTemplate;

    @Autowired
    UserRepository userRepository;

    @Autowired
    InvestorRepository investorRepository;

    @Override
    public Map getByUserId(Long id) {
        try {
            User user = userRepository.getById(id);

            return getByEmail(user.getEmail());
        } catch (Exception e) {
            e.printStackTrace();
            return responseTemplate.internalServerError(e);
        }
    }

    @Override
    public Map getByEmail(String email) {
        try {
            InvestorModel investorModel = new InvestorModel();

            User user = userRepository.findOneByEmail(email);

            Investor investor = investorRepository.getByUserId(user.getId());

            investorModel.setId(user.getId());
            investorModel.setEmail(user.getEmail());

            if (investor != null) {
                investorModel.setCitizenID(investor.getCitizenID());
                investorModel.setFullName(investor.getFullName());
                investorModel.setPhoneNumber(investor.getPhoneNumber());
                investorModel.setGender(investor.getGender());
                investorModel.setDateOfBirth(investor.getDateOfBirth());
                investorModel.setBankAccountNumber(investor.getBankAccountNumber());
            } else {
                investorModel.setCitizenID(null);
                investorModel.setFullName(null);
                investorModel.setPhoneNumber(null);
                investorModel.setGender(null);
                investorModel.setDateOfBirth(null);
                investorModel.setBankAccountNumber(null);
            }

            return responseTemplate.success(investorModel);
        } catch (Exception e) {
            e.printStackTrace();
            return responseTemplate.internalServerError(e);
        }
    }

    @Override
    public Map update(InvestorModel investorModel) {
        User user = userRepository.getById(investorModel.getId());
        Investor investor = investorRepository.getByUserId(user.getId());

        if (investor == null) {
            investor = new Investor();
            investor.setUser(user);
        }

        if (!StringUtils.isNumericSpace(investorModel.getBankAccountNumber())) return responseTemplate.notAllowed(
                "Bank account number is not numerical string");

        investor.setCitizenID(investorModel.getCitizenID());
        investor.setFullName(investorModel.getFullName());
        investor.setPhoneNumber(investorModel.getPhoneNumber());
        investor.setGender(investorModel.getGender());
        investor.setDateOfBirth(investorModel.getDateOfBirth());
        investor.setBankAccountNumber(investorModel.getBankAccountNumber());

        investorRepository.save(investor);

        return responseTemplate.success(null);
    }

    @Override
    public Map updateWithEmail(InvestorModel investorModel) {
        User user = userRepository.findOneByEmail(investorModel.getEmail());
        investorModel.setId(user.getId());

        return update(investorModel);
    }
}
