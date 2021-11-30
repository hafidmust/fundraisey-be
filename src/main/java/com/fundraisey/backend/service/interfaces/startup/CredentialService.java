package com.fundraisey.backend.service.interfaces.startup;

import com.fundraisey.backend.model.CredentialRequestModel;

import java.util.Map;

public interface CredentialService {
    Map insert(String email, CredentialRequestModel credentialRequestModel);
}
