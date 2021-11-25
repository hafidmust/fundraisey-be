package com.fundraisey.backend.model.startup;

import lombok.Data;

import java.util.Date;

@Data
public class ProductModel {
    private String name;
    private String description;
    private String url;
    private String productPhotoUrl;
}
