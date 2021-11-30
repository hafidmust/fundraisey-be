package com.fundraisey.backend.service.implementation.startup;

import com.fundraisey.backend.entity.auth.User;
import com.fundraisey.backend.entity.startup.Credential;
import com.fundraisey.backend.entity.startup.Product;
import com.fundraisey.backend.entity.startup.Startup;
import com.fundraisey.backend.model.startup.CredentialModel;
import com.fundraisey.backend.repository.auth.UserRepository;
import com.fundraisey.backend.repository.startup.CredentialRepository;
import com.fundraisey.backend.repository.startup.DocumentRepository;
import com.fundraisey.backend.repository.startup.StartupRepository;
import com.fundraisey.backend.service.startup.CredentialService;
import com.fundraisey.backend.util.ResponseTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Map;

@Slf4j
@Service
@Transactional
public class CredentialImplementation implements CredentialService {
    @Autowired
    CredentialRepository credentialRepository;

    @Autowired
    StartupRepository startupRepository;

    @Autowired
    DocumentRepository documentRepository;

    @Autowired
    UserRepository userRepository;

    ResponseTemplate responseTemplate = new ResponseTemplate();

    @Override
    public Map insert(CredentialModel credentialModel, Long id) {
        try {
            User user = userRepository.getById(id);

            if (user == null) return responseTemplate.notFound("Email Not Found");

            Credential credential = new Credential();

            Startup startup = startupRepository.getStartupProfileById(user.getId());

            credential.setStartup(startup);
            credential.setName(credentialModel.getName());
            credential.setDescription(credentialModel.getDescription());
            credential.setCredentialType(credentialModel.getCredentialType());
            credential.setIssue(credentialModel.getIssue());
            credential.setIssueDate(credentialModel.getIssueDate());
            credential.setExpirationDate(credentialModel.getExpirationDate());

            credentialRepository.save(credential);

            return responseTemplate.success(credential);
        } catch(Exception e) {
            log.error("Failed to save new credential with id {}: {}", id, e.getMessage());
            return responseTemplate.internalServerError(e.getLocalizedMessage());
        }
    }
}
