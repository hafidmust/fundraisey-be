package com.fundraisey.backend.model;

import com.fundraisey.backend.entity.auth.Gender;
import com.fundraisey.backend.entity.investor.InvestorVerification;
import lombok.Data;

import java.util.Date;

@Data
public class InvestorResponseModel {
    private Long id;
    private String email;
    private String citizenID;
    private String fullName;
    private String phoneNumber;
    private String bankAccountNumber;
    private Gender gender;
    private Date dateOfBirth;
    private boolean isVerified;
}
