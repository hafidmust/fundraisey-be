package com.fundraisey.backend.service.implementation.startup;

import com.fundraisey.backend.entity.auth.User;
import com.fundraisey.backend.entity.startup.Product;
import com.fundraisey.backend.entity.startup.Startup;
import com.fundraisey.backend.model.startup.ProductModel;
import com.fundraisey.backend.repository.auth.UserRepository;
import com.fundraisey.backend.repository.startup.ProductPhotoRepository;
import com.fundraisey.backend.repository.startup.ProductRepository;
import com.fundraisey.backend.repository.startup.StartupRepository;
import com.fundraisey.backend.service.interfaces.startup.ProductService;
import com.fundraisey.backend.util.ResponseTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional
public class ProductImplementation implements ProductService {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    StartupRepository startupRepository;

    @Autowired
    ProductPhotoRepository productPhotoRepository;

    @Autowired
    UserRepository userRepository;

    ResponseTemplate responseTemplate = new ResponseTemplate();

    @Override
    public Map getById(Long id) {
        return null;
    }

    @Override
    public Map insert(ProductModel productModel, Long id) {
        try {
            User user = userRepository.getById(id);

            if (user == null) return responseTemplate.notFound("Email Not Found");

            Product product = new Product();

            Startup startup = startupRepository.getStartupProfileById(user.getId());

            product.setStartup(startup);
            product.setName(productModel.getName());
            product.setDescription(productModel.getDescription());
            product.setUrl(productModel.getUrl());

            productRepository.save(product);

            return responseTemplate.success(product);
        } catch(Exception e) {
            e.printStackTrace();
            return responseTemplate.internalServerError(e.getLocalizedMessage());
        }
    }

    @Override
    public Map update(ProductModel productModel, Long id) {
        return null;
    }

    @Override
    public Map delete(Long productId, Long userId) {
        return null;
    }

    @Override
    public Map getAllByStartupId(Long startupId) {
        try {
            Startup startup = startupRepository.getById(startupId);
            if (startup == null) return responseTemplate.notFound("Startup not found");
            List<Product> products = productRepository.findByStartup(startup);

            return responseTemplate.success(products);
        } catch(Exception e) {
            e.printStackTrace();
            return responseTemplate.internalServerError(e.getLocalizedMessage());
        }
    }
}
