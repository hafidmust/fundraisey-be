package com.fundraisey.backend.service.interfaces;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface FileUploadService {
    Map uploadProductFile(String email, Long productId, MultipartFile file);
    Map uploadCredentialFile(String email, Long credentialId, MultipartFile file);
    Map uploadInvestorCitizenId(String email, MultipartFile file);
    Map uploadInvestorSelfie(String email, MultipartFile file);
    Map uploadStartupLogoFile(String email, MultipartFile file);
}
