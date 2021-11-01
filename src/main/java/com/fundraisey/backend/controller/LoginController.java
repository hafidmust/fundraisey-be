package com.fundraisey.backend.controller;

import com.fundraisey.backend.entity.auth.Role;
import com.fundraisey.backend.entity.auth.User;
import com.fundraisey.backend.model.LoginModel;
import com.fundraisey.backend.repository.auth.UserRepository;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1")
public class LoginController {
    @Value("${base_url}")
    private String baseUrl;

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    @Autowired
    private OAuth2UserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    private ResponseTemplate responseTemplate = new ResponseTemplate();

    Logger logger = LoggerFactory.getLogger(LoginController.class);

    @PostMapping("/login")
    public ResponseEntity<Map> login(@RequestBody LoginModel loginModel) {
        try {
            Map<String, Object> map = new HashMap<>();
            String url = baseUrl + "/oauth/token?username=" + loginModel.getEmail() +
                    "&password=" + loginModel.getPassword() +
                    "&grant_type=" + loginModel.getGrant_type() +
                    "&client_id=" + loginModel.getClient_id() +
                    "&client_secret=" + loginModel.getClient_secret();

            logger.info(url);
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

                return new ResponseEntity<Map>(responseTemplate.success(map), HttpStatus.OK);
            } else {
                return response;
            }
        } catch (Exception e) {
            e.printStackTrace();

            return new ResponseEntity<Map>(responseTemplate.internalServerError(e), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
