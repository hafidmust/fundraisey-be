package com.fundraisey.backend.service.implementation.startup;

import com.fundraisey.backend.entity.auth.User;
import com.fundraisey.backend.entity.startup.StartupNotification;
import com.fundraisey.backend.repository.auth.UserRepository;
import com.fundraisey.backend.repository.startup.StartupNotificationRepository;
import com.fundraisey.backend.repository.startup.StartupNotificationTypeRepository;
import com.fundraisey.backend.repository.startup.StartupRepository;
import com.fundraisey.backend.service.interfaces.startup.StartupNotificationService;
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
import java.util.Map;

@Slf4j
@Service
@Transactional
public class StartupNotificationImplementation implements StartupNotificationService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StartupRepository startupRepository;

    @Autowired
    private StartupNotificationRepository startupNotificationRepository;

    @Autowired
    private StartupNotificationTypeRepository startupNotificationTypeRepository;

    ResponseTemplate responseTemplate = new ResponseTemplate();

    @Override
    public Map getByUserId(Integer page, Integer size, String sortAttribute, String sortType, Principal principal) {
        Page<StartupNotification> startupNotifications;

        Pageable pageable;

        sortAttribute = sortAttribute.equals("") ? "id" : sortAttribute;

        try {
            User user = userRepository.findOneByEmail(principal.getName());

            if (user == null) return responseTemplate.notFound("Email not found");

            if ((sortType == "desc") || (sortType == "descending")) {
                pageable = PageRequest.of(page, size, Sort.by(sortAttribute).descending());
            } else {
                pageable = PageRequest.of(page, size, Sort.by(sortAttribute).ascending());
            }

            startupNotifications = startupNotificationRepository.findByUser(user.getId(), pageable);

            log.info("Get all startup notification success");

            return responseTemplate.success(startupNotifications);
        } catch (Exception e) {
            log.error("Failed to get all startup notification by user name {}: {}", principal.getName(), e.getMessage());
            return responseTemplate.internalServerError(e.getLocalizedMessage());
        }
    }
}
