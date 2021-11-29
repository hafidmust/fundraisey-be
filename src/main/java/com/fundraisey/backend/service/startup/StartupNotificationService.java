package com.fundraisey.backend.service.startup;

import java.security.Principal;
import java.util.Map;

public interface StartupNotificationService {
    Map getByUserId(Integer page, Integer size, String sortAttribute, String sortType, Principal principal);
}
