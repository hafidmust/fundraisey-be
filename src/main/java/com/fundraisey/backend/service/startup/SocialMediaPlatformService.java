package com.fundraisey.backend.service.startup;

import com.fundraisey.backend.entity.startup.SocialMediaPlatform;

import java.util.Map;

public interface SocialMediaPlatformService {
    public Map getAll();

    public Map insert(SocialMediaPlatform socialMediaPlatform);

    public Map update(SocialMediaPlatform socialMediaPlatform);

    public Map delete(Long id);

}
