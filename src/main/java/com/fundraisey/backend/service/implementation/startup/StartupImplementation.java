package com.fundraisey.backend.service.implementation.startup;

import com.fundraisey.backend.entity.auth.User;
import com.fundraisey.backend.entity.startup.*;
import com.fundraisey.backend.model.startup.StartupModel;
import com.fundraisey.backend.repository.auth.RoleRepository;
import com.fundraisey.backend.repository.auth.UserRepository;
import com.fundraisey.backend.repository.startup.*;
import com.fundraisey.backend.service.interfaces.startup.StartupService;
import com.fundraisey.backend.util.ResponseTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.*;

@Slf4j
@Service
@Transactional
public class StartupImplementation implements StartupService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private StartupRepository startupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CredentialRepository credentialRepository;

    @Autowired
    private SocialMediaRepository socialMediaRepository;

    @Autowired
    private SocialMediaPlatformRepository socialMediaPlatformRepository;

    @Autowired
    private ProductPhotoRepository productPhotoRepository;

    @Autowired
    private CredentialTypeRepository credentialTypeRepository;

    @Autowired
    private DocumentRepository documentRepository;

    ResponseTemplate responseTemplate = new ResponseTemplate();

    @Override
    public Map getAll(Integer page, Integer size, String sortAttribute, String sortType) {
        Page<Startup> startups;
        Pageable pageable;
        sortAttribute = sortAttribute.equals("") ? "id" : sortAttribute;
        try {
            if ((sortType == "desc") || (sortType == "descending")) {
                pageable = PageRequest.of(page, size, Sort.by(sortAttribute).descending());
            } else {
                pageable = PageRequest.of(page, size, Sort.by(sortAttribute).ascending());
            }

            startups = startupRepository.findAll(pageable);

            log.info("Get all startup success");

            return responseTemplate.success(startups);
        } catch (Exception e) {
            e.printStackTrace();
            return responseTemplate.internalServerError(e.getLocalizedMessage());
        }
    }

    @Override
    public Map getStartupById(Long id) {
        try {
            User user = userRepository.getById(id);

            if (user == null) return responseTemplate.notFound("Email Not Found");

            Startup startup = startupRepository.getStartupProfileById(id);

            return responseTemplate.success(startup);
        } catch (Exception e) {
            e.printStackTrace();
            return responseTemplate.internalServerError(e);
        }
    }

    @Override
    public Map insert(StartupModel startupModel, String email) {
        try {
            User user = userRepository.findOneByEmail(email);

            if (user == null) {
                return responseTemplate.notFound("User not found!");
            }

            Startup startup = new Startup();

            startup.setUser(user);
            startup.setName(startupModel.getName());
            startup.setDescription(startupModel.getDescription());
            startup.setPhoneNumber(startupModel.getPhoneNumber());
            startup.setWeb(startupModel.getWeb());
            startup.setAddress(startupModel.getAddress());
            startup.setFoundedDate(startupModel.getFoundedDate());
            startupRepository.save(startup);

            if (startupModel.getSocial_medias() != null) {
                for (SocialMedia indexSocialMedia : startupModel.getSocial_medias()) {
                    indexSocialMedia.setStartup(startup);
                    SocialMediaPlatform socialMediaPlatform = socialMediaPlatformRepository.getById(indexSocialMedia.getSocialMediaPlatform().getId());
                    indexSocialMedia.setSocialMediaPlatform(socialMediaPlatform);
                    socialMediaRepository.save(indexSocialMedia);
                }

                startup.setSocialMedias(startupModel.getSocial_medias());
            }

            if (startupModel.getProducts() != null) {
                for (Product indexProduct : startupModel.getProducts()) {
                    indexProduct.setStartup(startup);
                    productRepository.save(indexProduct);

                    if (indexProduct.getProductPhotos() != null) {
                        for (ProductPhoto indexProductPhoto : indexProduct.getProductPhotos()) {
                            indexProductPhoto.setUrl(indexProductPhoto.getUrl());
                            indexProductPhoto.setProduct(indexProduct);
                            productPhotoRepository.save(indexProductPhoto);
                        }
                    }
                }

                startup.setProducts(startupModel.getProducts());
            }
            if (startupModel.getCredentials() != null) {
                for (Credential indexCredential : startupModel.getCredentials()) {
                    indexCredential.setStartup(startup);

                    CredentialType credentialType = credentialTypeRepository.getById(indexCredential.getCredentialType().getId());
                    indexCredential.setCredentialType(credentialType);
                    credentialRepository.save(indexCredential);

                    if (indexCredential.getDocuments() != null) {
                        for (Document indexDocument : indexCredential.getDocuments()) {
                            indexDocument.setCredentialDocument(indexCredential);
                            documentRepository.save(indexDocument);
                        }
                    }
                }

                startup.setCredentials(startupModel.getCredentials());
            }

            return responseTemplate.success(startup);
        } catch (Exception e) {
            e.printStackTrace();
            return responseTemplate.internalServerError(e.getLocalizedMessage());
        }
    }

    @Override
    public Map update(StartupModel startupModel, String email) {
        try {
            User user = userRepository.findOneByEmail(email);

            if (user == null) {
                return responseTemplate.notFound("User not found!");
            }

            Startup startupExist = startupRepository.findByUser(user);

            if (startupExist == null) return responseTemplate.notFound("Startup not found!");

            startupExist.setName(startupModel.getName());
            startupExist.setDescription(startupModel.getDescription());
            startupExist.setPhoneNumber(startupModel.getPhoneNumber());
            startupExist.setWeb(startupModel.getWeb());
            startupExist.setAddress(startupModel.getAddress());
            startupExist.setFoundedDate(startupModel.getFoundedDate());
            startupRepository.save(startupExist);

            if (startupModel.getSocial_medias() != null) {
                for (SocialMedia indexSocialMedia : startupModel.getSocial_medias()) {
                    indexSocialMedia.setStartup(startupExist);
                    SocialMediaPlatform socialMediaPlatform = socialMediaPlatformRepository.getById(indexSocialMedia.getSocialMediaPlatform().getId());
                    indexSocialMedia.setSocialMediaPlatform(socialMediaPlatform);
                    socialMediaRepository.save(indexSocialMedia);
                }

                startupExist.setSocialMedias(startupModel.getSocial_medias());
            }

            if (startupModel.getProducts() != null) {
                for (Product indexProduct : startupModel.getProducts()) {
                    indexProduct.setStartup(startupExist);
                    productRepository.save(indexProduct);

                    if (indexProduct.getProductPhotos() != null) {
                        for (ProductPhoto indexProductPhoto : indexProduct.getProductPhotos()) {
                            indexProductPhoto.setUrl(indexProductPhoto.getUrl());
                            indexProductPhoto.setProduct(indexProduct);
                            productPhotoRepository.save(indexProductPhoto);
                        }
                    }
                }

                startupExist.setProducts(startupModel.getProducts());
            }
            if (startupModel.getCredentials() != null) {
                for (Credential indexCredential : startupModel.getCredentials()) {
                    indexCredential.setStartup(startupExist);

                    CredentialType credentialType = credentialTypeRepository.getById(indexCredential.getCredentialType().getId());
                    indexCredential.setCredentialType(credentialType);
                    credentialRepository.save(indexCredential);

                    if (indexCredential.getDocuments() != null) {
                        for (Document indexDocument : indexCredential.getDocuments()) {
                            indexDocument.setCredentialDocument(indexCredential);
                            documentRepository.save(indexDocument);
                        }
                    }
                }

                startupExist.setCredentials(startupModel.getCredentials());
            }

            log.info("Update startup info with startup id {}", startupModel.getId());

            return responseTemplate.success("Startup is updated!");

        } catch (Exception e) {
            e.printStackTrace();
            return responseTemplate.internalServerError(e.getLocalizedMessage());
        }
    }

    @Override
    public Map delete(Long startupId, String email) {
        try {
            User user = userRepository.findOneByEmail(email);

            if (user == null) {
                return responseTemplate.notFound("User not found!");
            }

            Startup startupExist = startupRepository.getStartupProfileById(startupId);

            if (startupExist == null) return responseTemplate.notFound("Startup not found!");

            Date now = new Date();
            startupExist.setDeleted_at(now);

            log.info("Delete startup with id {}", startupId);
            startupRepository.save(startupExist);

            return responseTemplate.success("This startup is deleted!");
        } catch (Exception e) {
            log.error("Failed to delete startup id {}: {}", startupId, e.getMessage());
            return responseTemplate.internalServerError(e.getLocalizedMessage());
        }
    }
}
