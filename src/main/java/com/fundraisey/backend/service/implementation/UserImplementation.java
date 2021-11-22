package com.fundraisey.backend.service.implementation;


import com.fundraisey.backend.entity.auth.Role;
import com.fundraisey.backend.entity.auth.User;
import com.fundraisey.backend.entity.investor.Investor;
import com.fundraisey.backend.model.UserModel;
import com.fundraisey.backend.repository.investor.InvestorRepository;
import com.fundraisey.backend.repository.auth.RoleRepository;
import com.fundraisey.backend.repository.auth.UserRepository;
import com.fundraisey.backend.service.implementation.auth.LoginImplementation;
import com.fundraisey.backend.service.interfaces.UserService;
import com.fundraisey.backend.util.ResponseTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class UserImplementation implements UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    InvestorRepository investorRepository;

    @Autowired
    RoleRepository roleRepository;

    ResponseTemplate responseTemplate = new ResponseTemplate();

    Logger logger = LoggerFactory.getLogger(LoginImplementation.class);

    @Override
    public Map getAllByRole(Integer page, Integer size, String sortAttribute, String sortType, String role) {
        Page<User> users;
        Pageable options;
        sortAttribute = sortAttribute.equals("") ? "id" : sortAttribute;
        try {
            List<UserModel> response = new ArrayList<>();
            String[] roleNames = {role};
            List<Role> roles = roleRepository.findByNameIn(roleNames);

            if ((sortType == "desc") || (sortType == "descending")) {
                options = PageRequest.of(page, size, Sort.by(sortAttribute).descending());
            } else {
                options = PageRequest.of(page, size, Sort.by(sortAttribute).ascending());
            }
            users = userRepository.findByRolesIn(roles, options);

            for (User user : users.getContent()) {
                UserModel userModel = new UserModel();

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

                response.add(userModel);
            }

            return responseTemplate.success(response);
        } catch (Exception e) {
            e.printStackTrace();
            return responseTemplate.internalServerError(e);
        }
    }

    @Override
    public Map getById(Long id) {
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
    public Map update(UserModel userModel) {
        User user = userRepository.getById(userModel.getId());
        Investor investor = investorRepository.getByUserId(user.getId());

        if (investor == null) {
            investor = new Investor();
            investor.setUser(user);
        }

        investor.setCitizenID(userModel.getCitizenID());
        investor.setFullName(userModel.getFullName());
        investor.setPhoneNumber(userModel.getPhoneNumber());
        investor.setGender(userModel.getGender());
        investor.setDateOfBirth(userModel.getDateOfBirth());

        investorRepository.save(investor);

        return responseTemplate.success(null);
    }

    @Override
    public Map updateWithEmail(UserModel userModel) {
        User user = userRepository.findOneByEmail(userModel.getEmail());
        userModel.setId(user.getId());

        return update(userModel);
    }
}
