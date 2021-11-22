package com.fundraisey.backend.service.implementation.auth;

import com.fundraisey.backend.entity.auth.Role;
import com.fundraisey.backend.entity.auth.User;
import com.fundraisey.backend.model.LoginModel;
import com.fundraisey.backend.repository.auth.UserRepository;
import com.fundraisey.backend.service.interfaces.auth.LoginService;
import com.fundraisey.backend.service.OAuth2UserDetailsService;
import com.fundraisey.backend.util.ResponseTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LoginImplementation implements LoginService {
    @Value("${base_url}")
    private String baseUrl;

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    @Autowired
    private OAuth2UserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    private ResponseTemplate responseTemplate = new ResponseTemplate();

    Logger logger = LoggerFactory.getLogger(LoginImplementation.class);

    @Override
    public Map login(LoginModel loginModel) {
        try {
            Map<String, Object> map = new HashMap<>();

            User checkUser = userRepository.findOneByEmail(loginModel.getEmail());

            if ((checkUser != null) && (encoder.matches(loginModel.getPassword(),checkUser.getPassword()))) {
                if (!checkUser.isEnabled()) {
                    map.put("is_enabled", checkUser.isEnabled());
                    return responseTemplate.success(map);
                }
            }
            if (checkUser == null){
                return responseTemplate.notFound("user not found");
            }
            if (!(encoder.matches(loginModel.getPassword(),checkUser.getPassword()))) {
                return responseTemplate.isRequired("wrong password");
            }

            String url = baseUrl + "/oauth/token?username=" + loginModel.getEmail() +
                    "&password=" + loginModel.getPassword() +
                    "&grant_type=" + loginModel.getGrant_type() +
                    "&client_id=" + loginModel.getClient_id() +
                    "&client_secret=" + loginModel.getClient_secret();

            // logger.info(url);
            ResponseEntity<Map> response = restTemplateBuilder.build().exchange(url, HttpMethod.POST, null, new
                    ParameterizedTypeReference<Map>() {
                    });

            if (response.getStatusCode() == HttpStatus.OK) {
                User user = userRepository.findOneByEmail(loginModel.getEmail());
                List<String> roles = new ArrayList<>();

                for (Role role : user.getRoles()) {
                    roles.add(role.getName());
                }

                map.put("access_token", response.getBody().get("access_token"));
                map.put("roles", roles);
                map.put("is_enabled", user.isEnabled());

                return responseTemplate.success(map);
            } else {
                return responseTemplate.notFound("user not found");
            }
        } catch (HttpStatusCodeException e) {
            e.printStackTrace();
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                return responseTemplate.isRequired("invalid login");
            }
            return responseTemplate.internalServerError(e);
        } catch (Exception e) {
            e.printStackTrace();

            return responseTemplate.internalServerError(e);
        }
    }
}
