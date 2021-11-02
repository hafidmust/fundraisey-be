package com.fundraisey.backend.model;

import lombok.Data;

@Data
public class LoginResponseModel {
    private String access_token;
    private String[] roles;
    private boolean is_enabled;
}
