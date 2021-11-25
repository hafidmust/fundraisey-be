package com.fundraisey.backend.service.implementation.startup;

import com.fundraisey.backend.entity.startup.CredentialType;
import com.fundraisey.backend.repository.startup.CredentialTypeRepository;
import com.fundraisey.backend.service.startup.CredentialTypeService;
import com.fundraisey.backend.util.ResponseTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional
public class CredentialTypeImplementation implements CredentialTypeService {
    @Autowired
    CredentialTypeRepository credentialTypeRepository;

    ResponseTemplate responseTemplate = new ResponseTemplate();

    @Override
    public Map getAll() {
        try {
            log.info("Get all credential type");
            List<CredentialType> credentialTypeList = credentialTypeRepository.findAll();

            return responseTemplate.success(credentialTypeList);
        } catch (Exception e) {
            log.error("Failed to get all credential type: {}", e.getMessage());
            return responseTemplate.internalServerError(e.getLocalizedMessage());
        }

    }

    @Override
    public Map insert(CredentialType credentialType) {
        try {
            log.info("Save new credential type {}", credentialType.getName());
            credentialTypeRepository.save(credentialType);

            return responseTemplate.success(credentialType);
        } catch (Exception e) {
            log.error("Failed to save new credential type {}: {}", credentialType.getName(), e.getMessage());
            return responseTemplate.internalServerError(e.getLocalizedMessage());
        }

    }

    @Override
    public Map update(CredentialType credentialType) {
        try {
            boolean credentialTypeIsExist = credentialTypeRepository.existsById(credentialType.getId());
            if (!credentialTypeIsExist) return responseTemplate.notFound("This credential type Not Found");

            CredentialType credentialTypeExist = credentialTypeRepository.getById(credentialType.getId());
            credentialTypeExist.setName(credentialType.getName());

            log.info("Update credential type {}", credentialType.getName());
            credentialTypeRepository.save(credentialType);

            return responseTemplate.success("credential type is updated");
        } catch (Exception e) {
            log.error("Failed to update credential type {}: {}", credentialType.getName(), e.getMessage());
            return responseTemplate.internalServerError(e.getLocalizedMessage());
        }

    }

    @Override
    public Map delete(Long id) {
        try {
            boolean credentialTypeIsExist = credentialTypeRepository.existsById(id);
            if (!credentialTypeIsExist) return responseTemplate.notFound("This credential type Not Found");

            CredentialType credentialTypeExist = credentialTypeRepository.getById(id);
            Date now = new Date();
            credentialTypeExist.setDeleted_at(now);

            log.info("Delete credential type with id {}", id);
            credentialTypeRepository.save(credentialTypeExist);

            return responseTemplate.success("credential type is deleted");
        } catch (Exception e) {
            log.error("Failed to delete credential type with id {}: {}", id, e.getMessage());
            return responseTemplate.internalServerError(e.getLocalizedMessage());
        }

    }
}
