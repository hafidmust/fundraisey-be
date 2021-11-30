package com.fundraisey.backend.service.interfaces.startup;

import java.security.Principal;
import java.util.Map;

public interface StartupNotificationService {
    Map getByUserId(Integer page, Integer size, String sortAttribute, String sortType, Principal principal);
}
