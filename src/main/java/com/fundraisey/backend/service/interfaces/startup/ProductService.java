package com.fundraisey.backend.service.interfaces.startup;

import com.fundraisey.backend.model.startup.ProductModel;

import java.util.Map;

public interface ProductService {
    Map getById(Long id);
    Map insert(ProductModel productModel, Long id);
    Map update(ProductModel productModel, Long id);
    Map delete(Long productId, Long userId);
    Map getAllByStartupId(Long startupId);
}
