package com.fundraisey.backend.model.startup;

import com.fundraisey.backend.entity.startup.Credential;
import com.fundraisey.backend.entity.startup.Product;
import com.fundraisey.backend.entity.startup.SocialMedia;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class StartupModel {
    private Long id;
    private String name;
    private String description;
    private String phoneNumber;
    private String web;
    private String address;
    private Date foundedDate;

    // Social Media
    private List<SocialMedia> social_medias;

    // Product
    private List<Product> products;

    // Credential
    private List<Credential> credentials;
}
