package com.fundraisey.backend.service.implementation.startup;

import com.fundraisey.backend.entity.auth.User;
import com.fundraisey.backend.entity.startup.Startup;
import com.fundraisey.backend.repository.auth.UserRepository;
import com.fundraisey.backend.repository.startup.HomeRepository;
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
    HomeRepository homeRepository;

    @Autowired
    UserRepository userRepository;

    ResponseTemplate responseTemplate = new ResponseTemplate();

    @Override
    public Map getProductsById(Principal principal) {
        try {
            User user = userRepository.findOneByEmail(principal.getName());

            if (user == null) return responseTemplate.notFound("User tidak ditemukan");

            List<Startup> productList = new ArrayList<>();

            productList = homeRepository.getAllProductByStartupId(user.getId());

            return responseTemplate.success(productList);
        } catch (Exception e) {
            e.printStackTrace();
            return responseTemplate.internalServerError(e);
        }
    }
}
