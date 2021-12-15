package com.fundraisey.backend.service.interfaces;

import com.fundraisey.backend.model.UserModel;

import java.security.Principal;
import java.util.Map;

public interface UserService {
    Map getAllByRole(Integer page, Integer size, String sortAttribute, String sortType, String role);
    Map getById(Long id);
    Map getByEmail(String email);
    Map update(UserModel userModel);
    Map updateWithEmail(UserModel userModel);
    Long getUserById(Principal principal);
}
