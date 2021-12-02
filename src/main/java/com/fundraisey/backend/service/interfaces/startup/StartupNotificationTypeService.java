package com.fundraisey.backend.service.interfaces.startup;

import com.fundraisey.backend.entity.startup.StartupNotificationType;

import java.util.Map;

public interface StartupNotificationTypeService {
    public Map getAll();

    public Map insert(StartupNotificationType startupNotificationType);

    public Map update(StartupNotificationType startupNotificationType);

    public Map delete(Long id);
}
