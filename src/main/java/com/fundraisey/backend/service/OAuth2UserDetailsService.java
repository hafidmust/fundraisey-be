package com.fundraisey.backend.service;

import com.fundraisey.backend.entity.auth.User;
import com.fundraisey.backend.repository.auth.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class OAuth2UserDetailsService implements UserDetailsService{

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findOneByEmail(s);
        if (null == user) {
            throw new UsernameNotFoundException(String.format("Email %s not found", s));
        }

        return user;
    }
}

