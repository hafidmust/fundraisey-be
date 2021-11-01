package com.fundraisey.backend.model;

import lombok.Data;

@Data
public class LoginModel {
    private String email;
    private String password;
    private String grant_type;
    private String client_id;
    private String client_secret;
}
