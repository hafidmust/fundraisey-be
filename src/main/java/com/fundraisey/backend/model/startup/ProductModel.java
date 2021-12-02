package com.fundraisey.backend.model.startup;

import com.fundraisey.backend.entity.startup.ProductPhoto;
import lombok.Data;

import java.util.Date;

@Data
public class ProductModel {
    private String id;
    private String name;
    private String description;
    private String url;
    private ProductPhoto productPhoto;
}
