package com.fundraisey.backend.service.startup;

import com.fundraisey.backend.model.startup.ProductModel;
import com.fundraisey.backend.model.startup.StartupModel;

import java.security.Principal;
import java.util.Map;

public interface StartupService {
    Map getStartupById(Long id);
    Map insert(StartupModel startupModel, Long id);
    Map update(StartupModel startupModel, Long id);
    Map delete(Long startupId, Long userId);
}
