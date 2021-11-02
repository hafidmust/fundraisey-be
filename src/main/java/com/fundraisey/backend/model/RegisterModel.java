package com.fundraisey.backend.model;

import com.fundraisey.backend.entity.auth.Gender;
import lombok.Data;

import java.util.Date;

@Data
public class RegisterModel {
    private String email;
    private String password;
    private String citizenID;
    private String fullName;
    private String phoneNumber;
    private Gender gender;
    private Date dateOfBirth;
}
