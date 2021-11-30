package com.fundraisey.backend.service.interfaces.startup;

import java.security.Principal;
import java.util.Map;

public interface HomeService {
    Map getIndexData(String email);
}
