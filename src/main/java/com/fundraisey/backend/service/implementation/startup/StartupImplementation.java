package com.fundraisey.backend.service.implementation.startup;

import com.fundraisey.backend.entity.auth.Role;
import com.fundraisey.backend.entity.auth.User;
import com.fundraisey.backend.entity.startup.SocialMediaPlatform;
import com.fundraisey.backend.entity.startup.Startup;
import com.fundraisey.backend.model.startup.ProductModel;
import com.fundraisey.backend.model.startup.StartupModel;
import com.fundraisey.backend.repository.auth.RoleRepository;
import com.fundraisey.backend.repository.auth.UserRepository;
import com.fundraisey.backend.repository.startup.StartupRepository;
import com.fundraisey.backend.service.startup.StartupService;
import com.fundraisey.backend.util.ResponseTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class StartupImplementation implements StartupService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private StartupRepository startupRepository;

    ResponseTemplate responseTemplate = new ResponseTemplate();

    @Override
    public Map getStartupById(Long id) {
        try {
            User user = userRepository.getById(id);

            if (user == null) return responseTemplate.notFound("Email Not Found");

            Startup startup = startupRepository.getStartupProfileById(id);

            return responseTemplate.success(startup);
        } catch (Exception e) {
            e.printStackTrace();
            return responseTemplate.internalServerError(e);
        }
    }

    @Override
    public Map insert(StartupModel startupModel, Long id) {
        try {
            boolean userIsExist = userRepository.existsById(id);

            if (!userIsExist) {
                return responseTemplate.notFound("User not found!");
            }

            Startup startupExist = startupRepository.findOneByName(startupModel.getName());

            if (startupExist != null) {
                return responseTemplate.alreadyExist("Startup already registered!");
            }

            User user = userRepository.getById(id);

            Startup startup = new Startup();

            startup.setUser(user);
            startup.setName(startupModel.getName());
            startup.setDescription(startupModel.getDescription());
            startup.setLogo(startupModel.getLogo());
            startup.setPhoneNumber(startupModel.getPhoneNumber());
            startup.setWeb(startupModel.getWeb());
            startup.setAddress(startupModel.getAddress());
            startup.setFoundedDate(startupModel.getFoundedDate());

            startupRepository.save(startup);

            return responseTemplate.success(startup);
        } catch (Exception e) {
            e.printStackTrace();
            return responseTemplate.internalServerError(e);
        }
    }

    @Override
    public Map update(StartupModel startupModel, Long id) {
        return null;
    }

    @Override
    public Map delete(Long startupId, Long userId) {
        return null;
    }
}
