package com.fundraisey.backend.service.implementation.startup;

import com.fundraisey.backend.entity.startup.StartupNotificationType;
import com.fundraisey.backend.repository.startup.StartupNotificationTypeRepository;
import com.fundraisey.backend.service.interfaces.startup.StartupNotificationTypeService;
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

public class StartupNotificationTypeImplementation implements StartupNotificationTypeService {
    @Autowired
    StartupNotificationTypeRepository startupNotificationTypeRepository;

    ResponseTemplate responseTemplate = new ResponseTemplate();

    @Override
    public Map getAll() {
        try {
            log.info("Get all startup notification type");
            List<StartupNotificationType> startupNotificationTypeList = startupNotificationTypeRepository.findAll();

            return responseTemplate.success(startupNotificationTypeList);
        } catch (Exception e) {
            log.error("Failed to get all startup notification type: {}", e.getMessage());
            return responseTemplate.internalServerError(e.getLocalizedMessage());
        }
    }

    @Override
    public Map insert(StartupNotificationType startupNotificationType) {
        try {
            log.info("Save new startup notification type {}", startupNotificationType.getName());
            startupNotificationTypeRepository.save(startupNotificationType);

            return responseTemplate.success(startupNotificationType);
        } catch (Exception e) {
            log.error("Failed to save new startup notification type {}: {}", startupNotificationType.getName(), e.getMessage());
            return responseTemplate.internalServerError(e.getLocalizedMessage());
        }
    }

    @Override
    public Map update(StartupNotificationType startupNotificationType) {
        try {
            boolean startupNotificationTypeIsExist = startupNotificationTypeRepository.existsById(startupNotificationType.getId());

            if (!startupNotificationTypeIsExist) return responseTemplate.notFound("This startup notification type not found");

            StartupNotificationType startupNotificationTypeExist = startupNotificationTypeRepository.getById(startupNotificationType.getId());
            startupNotificationTypeExist.setName(startupNotificationType.getName());

            log.info("Update startup notification type {}", startupNotificationType.getName());
            startupNotificationTypeRepository.save(startupNotificationType);

            return responseTemplate.success("Startup Notification Type is updated");
        } catch (Exception e) {
            log.error("Failed to update startup notification type {}: {}", startupNotificationType.getName(), e.getMessage());
            return responseTemplate.internalServerError(e.getLocalizedMessage());
        }
    }

    @Override
    public Map delete(Long id) {
        try {
            boolean startupNotificationTypeIsExist = startupNotificationTypeRepository.existsById(id);

            if (!startupNotificationTypeIsExist) return responseTemplate.notFound("This startup notification type not found");

            StartupNotificationType startupNotificationTypeExist = startupNotificationTypeRepository.getById(id);
            Date now = new Date();
            startupNotificationTypeExist.setDeleted_at(now);

            log.info("Delete startup notification type with id {}", id);
            startupNotificationTypeRepository.save(startupNotificationTypeExist);

            return responseTemplate.success("Startup Notification Type is deleted");
        } catch (Exception e) {
            log.error("Failed to delete startup notification type with id {}: {}", id, e.getMessage());
            return responseTemplate.internalServerError(e.getLocalizedMessage());
        }
    }
}
