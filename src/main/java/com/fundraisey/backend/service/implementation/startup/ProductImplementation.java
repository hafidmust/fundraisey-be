package com.fundraisey.backend.service.implementation.startup;

import com.fundraisey.backend.entity.startup.Product;
import com.fundraisey.backend.model.startup.ProductModel;
import com.fundraisey.backend.repository.startup.ProductRepository;
import com.fundraisey.backend.repository.startup.StartupRepository;
import com.fundraisey.backend.service.startup.ProductService;
import com.fundraisey.backend.util.ResponseTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ProductImplementation implements ProductService {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    StartupRepository startupRepository;

    ResponseTemplate responseTemplate = new ResponseTemplate();

    @Override
    public Map getById(Long id) {
        return null;
    }

    @Override
    public Map insert(ProductModel productModel, Long id) {
        return null;
    }

    @Override
    public Map update(ProductModel productModel, Long id) {
        return null;
    }

    @Override
    public Map delete(Long productId, Long userId) {
        return null;
    }
}
