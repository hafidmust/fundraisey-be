package com.fundraisey.backend.service.implementation;


import com.fundraisey.backend.entity.auth.Role;
import com.fundraisey.backend.entity.auth.User;
import com.fundraisey.backend.entity.auth.UserDetail;
import com.fundraisey.backend.model.UserModel;
import com.fundraisey.backend.repository.UserDetailRepository;
import com.fundraisey.backend.repository.auth.RoleRepository;
import com.fundraisey.backend.repository.auth.UserRepository;
import com.fundraisey.backend.service.UserService;
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
    UserDetailRepository userDetailRepository;

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

                UserDetail userDetail = userDetailRepository.getByUserId(user.getId());

                userModel.setId(user.getId());
                userModel.setEmail(user.getEmail());

                if (userDetail != null) {
                    userModel.setCitizenID(userDetail.getCitizenID());
                    userModel.setFullName(userDetail.getFullName());
                    userModel.setPhoneNumber(userDetail.getPhoneNumber());
                    userModel.setGender(userDetail.getGender());
                    userModel.setDateOfBirth(userDetail.getDateOfBirth());
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

            UserDetail userDetail = userDetailRepository.getByUserId(user.getId());

            userModel.setId(user.getId());
            userModel.setEmail(user.getEmail());

            if (userDetail != null) {
                userModel.setCitizenID(userDetail.getCitizenID());
                userModel.setFullName(userDetail.getFullName());
                userModel.setPhoneNumber(userDetail.getPhoneNumber());
                userModel.setGender(userDetail.getGender());
                userModel.setDateOfBirth(userDetail.getDateOfBirth());
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
        UserDetail userDetail = userDetailRepository.getByUserId(user.getId());

        if (userDetail == null) {
            userDetail = new UserDetail();
            userDetail.setUser(user);
        }

        userDetail.setCitizenID(userModel.getCitizenID());
        userDetail.setFullName(userModel.getFullName());
        userDetail.setPhoneNumber(userModel.getPhoneNumber());
        userDetail.setGender(userModel.getGender());
        userDetail.setDateOfBirth(userModel.getDateOfBirth());

        userDetailRepository.save(userDetail);

        return responseTemplate.success(null);
    }

    @Override
    public Map updateWithEmail(UserModel userModel) {
        User user = userRepository.findOneByEmail(userModel.getEmail());
        userModel.setId(user.getId());

        return update(userModel);
    }
}
