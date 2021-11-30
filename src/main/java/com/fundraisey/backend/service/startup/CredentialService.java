package com.fundraisey.backend.service.startup;

import com.fundraisey.backend.model.startup.CredentialModel;

import java.util.Map;

public interface CredentialService {
    Map insert(CredentialModel credentialModel, Long id);
}
