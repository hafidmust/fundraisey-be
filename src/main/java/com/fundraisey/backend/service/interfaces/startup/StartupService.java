package com.fundraisey.backend.service.interfaces.startup;

import com.fundraisey.backend.model.startup.StartupModel;

import java.security.Principal;
import java.util.Map;

public interface StartupService {
    Map getStartupById(Long id);
    Map getByUserId(Integer page, Integer size, String sortAttribute, String sortType, Principal principal);
    Map insert(StartupModel startupModel, Long id);
    Map update(StartupModel startupModel, Long id);
    Map delete(Long startupId, Long userId);
}
