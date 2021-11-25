package com.fundraisey.backend.service.implementation.startup;

import com.fundraisey.backend.entity.startup.SocialMediaPlatform;
import com.fundraisey.backend.repository.startup.SocialMediaPlatformRepository;
import com.fundraisey.backend.service.startup.SocialMediaPlatformService;
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
public class SocialMediaPlatformImplementation implements SocialMediaPlatformService {
    @Autowired
    SocialMediaPlatformRepository socialMediaPlatformRepository;

    ResponseTemplate responseTemplate = new ResponseTemplate();

    @Override
    public Map getAll() {
        try {
            log.info("Get all social media platform");
            List<SocialMediaPlatform> socialMediaPlatformList = socialMediaPlatformRepository.findAll();

            return responseTemplate.success(socialMediaPlatformList);
        } catch (Exception e) {
            log.error("Failed to get all social media platform: {}", e.getMessage());
            return responseTemplate.internalServerError(e.getLocalizedMessage());
        }

    }

    @Override
    public Map insert(SocialMediaPlatform socialMediaPlatform) {
        try {
            log.info("Save new social media platform {}", socialMediaPlatform.getName());
            socialMediaPlatformRepository.save(socialMediaPlatform);

            return responseTemplate.success(socialMediaPlatform);
        } catch (Exception e) {
            log.error("Failed to save new social media platform {}: {}", socialMediaPlatform.getName(), e.getMessage());
            return responseTemplate.internalServerError(e.getLocalizedMessage());
        }

    }

    @Override
    public Map update(SocialMediaPlatform socialMediaPlatform) {
        try {
            boolean socialMediaPlatformIsExist = socialMediaPlatformRepository.existsById(socialMediaPlatform.getId());
            if (!socialMediaPlatformIsExist) return responseTemplate.notFound("This Social Media Platform Not Found");

            SocialMediaPlatform socialMediaPlatformExist = socialMediaPlatformRepository.getById(socialMediaPlatform.getId());
            socialMediaPlatformExist.setName(socialMediaPlatform.getName());
            socialMediaPlatformExist.setLogo(socialMediaPlatform.getLogo());
            socialMediaPlatformExist.setWebsite(socialMediaPlatform.getWebsite());

            log.info("Update Social Media Platform {}", socialMediaPlatform.getName());
            socialMediaPlatformRepository.save(socialMediaPlatform);

            return responseTemplate.success("Social Media Platform is updated");
        } catch (Exception e) {
            log.error("Failed to update Social Media Platform {}: {}", socialMediaPlatform.getName(), e.getMessage());
            return responseTemplate.internalServerError(e.getLocalizedMessage());
        }

    }

    @Override
    public Map delete(Long id) {
        try {
            boolean socialMediaPlatformIsExist = socialMediaPlatformRepository.existsById(id);
            if (!socialMediaPlatformIsExist) return responseTemplate.notFound("This Social Media Platform Not Found");

            SocialMediaPlatform socialMediaPlatformExist = socialMediaPlatformRepository.getById(id);
            Date now = new Date();
            socialMediaPlatformExist.setDeleted_at(now);

            log.info("Delete social media platform with id {}", id);
            socialMediaPlatformRepository.save(socialMediaPlatformExist);

            return responseTemplate.success("Social Media Platform is deleted");
        } catch (Exception e) {
            log.error("Failed to delete Social Media Platform with id {}: {}", id, e.getMessage());
            return responseTemplate.internalServerError(e.getLocalizedMessage());
        }

    }
}
