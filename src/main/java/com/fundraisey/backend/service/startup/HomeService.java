package com.fundraisey.backend.service.startup;

import java.security.Principal;
import java.util.Map;

public interface HomeService {
    Map getProductsById(Principal principal);
}
