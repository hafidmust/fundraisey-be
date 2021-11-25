package com.fundraisey.backend.model.startup;

import com.fundraisey.backend.entity.startup.SocialMedia;
import lombok.Data;

import java.util.Date;

@Data
public class StartupModel {
    private String name;
    private String description;
    private String logo;
    private String phoneNumber;
    private String web;
    private String address;
    private Date foundedDate;

    // Social Media
    private SocialMedia socialMedia;
}
