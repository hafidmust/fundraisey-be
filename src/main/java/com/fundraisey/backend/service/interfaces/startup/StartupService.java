package com.fundraisey.backend.service.interfaces.startup;

import com.fundraisey.backend.model.startup.StartupModel;

import java.security.Principal;
import java.util.Map;

public interface StartupService {
    Map getStartupById(Long id);
    Map getAll(Integer page, Integer size, String sortAttribute, String sortType);
    Map insert(StartupModel startupModel, String email);
    Map update(StartupModel startupModel, String email);
    Map delete(Long startupId, String email);
}
