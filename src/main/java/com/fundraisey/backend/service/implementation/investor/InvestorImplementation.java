package com.fundraisey.backend.service.implementation.investor;

import com.fundraisey.backend.entity.auth.User;
import com.fundraisey.backend.entity.investor.Investor;
import com.fundraisey.backend.model.InvestorModel;
import com.fundraisey.backend.model.UserModel;
import com.fundraisey.backend.repository.investor.InvestorRepository;
import com.fundraisey.backend.repository.auth.UserRepository;
import com.fundraisey.backend.service.interfaces.investor.InvestorService;
import com.fundraisey.backend.util.ResponseTemplate;
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
            UserModel userModel = new UserModel();

            User user = userRepository.findOneByEmail(email);

            Investor investor = investorRepository.getByUserId(user.getId());

            userModel.setId(user.getId());
            userModel.setEmail(user.getEmail());

            if (investor != null) {
                userModel.setCitizenID(investor.getCitizenID());
                userModel.setFullName(investor.getFullName());
                userModel.setPhoneNumber(investor.getPhoneNumber());
                userModel.setGender(investor.getGender());
                userModel.setDateOfBirth(investor.getDateOfBirth());
            } else {
                userModel.setCitizenID(null);
                userModel.setFullName(null);
                userModel.setPhoneNumber(null);
                userModel.setGender(null);
                userModel.setDateOfBirth(null);
            }

            return responseTemplate.success(userModel);
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

        investor.setCitizenID(investorModel.getCitizenID());
        investor.setFullName(investorModel.getFullName());
        investor.setPhoneNumber(investorModel.getPhoneNumber());
        investor.setGender(investorModel.getGender());
        investor.setDateOfBirth(investorModel.getDateOfBirth());

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
