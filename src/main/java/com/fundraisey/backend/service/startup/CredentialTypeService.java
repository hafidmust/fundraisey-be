package com.fundraisey.backend.service.startup;

import com.fundraisey.backend.entity.startup.CredentialType;

import java.util.Map;

public interface CredentialTypeService {
    public Map getAll();

    public Map insert(CredentialType credentialType);

    public Map update(CredentialType credentialType);

    public Map delete(Long id);
}
