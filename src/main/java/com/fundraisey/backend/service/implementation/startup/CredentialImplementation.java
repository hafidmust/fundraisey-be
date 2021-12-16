package com.fundraisey.backend.service.implementation.startup;

import com.fundraisey.backend.entity.auth.User;
import com.fundraisey.backend.entity.startup.Credential;
import com.fundraisey.backend.entity.startup.CredentialStatus;
import com.fundraisey.backend.entity.startup.Startup;
import com.fundraisey.backend.model.CredentialRequestModel;
import com.fundraisey.backend.repository.auth.UserRepository;
import com.fundraisey.backend.repository.startup.CredentialRepository;
import com.fundraisey.backend.repository.startup.CredentialTypeRepository;
import com.fundraisey.backend.repository.startup.StartupRepository;
import com.fundraisey.backend.service.interfaces.startup.CredentialService;
import com.fundraisey.backend.util.ResponseTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CredentialImplementation implements CredentialService {
    @Autowired
    CredentialRepository credentialRepository;
    @Autowired
    CredentialTypeRepository credentialTypeRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    StartupRepository startupRepository;

    @Autowired
    ResponseTemplate responseTemplate;

    @Override
    public Map insert(String email, CredentialRequestModel credentialRequestModel) {
        try {
            User user = userRepository.findOneByEmail(email);
            Startup startup = startupRepository.findByUser(user);

            Credential credential = new Credential();
            credential.setStartup(startup);
            credential.setName(credentialRequestModel.getName());
            credential.setIssuingOrganization(credentialRequestModel.getIssuingOrganization());
            credential.setIssueDate(credentialRequestModel.getIssueDate());
            credential.setExpirationDate(credentialRequestModel.getExpirationDate());
            credential.setCredentialUrl(credentialRequestModel.getCredentialUrl());
            credential.setCredentialType(credentialTypeRepository.getById(credentialRequestModel.getCredentialTypeId()));
            credential.setDescription(credentialRequestModel.getCredentialDescription());
            credential.setCredentialId(credentialRequestModel.getCredentialId());
            credential.setStatus(CredentialStatus.pending);

            Credential saved = credentialRepository.save(credential);

            return responseTemplate.success(saved);
        } catch (Exception e) {
            e.printStackTrace();
            return responseTemplate.internalServerError(e);
        }
    }
}
