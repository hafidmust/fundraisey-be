package com.fundraisey.backend.controller;

import com.fundraisey.backend.service.interfaces.FileUploadService;
import com.fundraisey.backend.util.ResponseTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/v1/upload")
public class FileUploadController {
    @Autowired
    FileUploadService fileUploadService;
    @Autowired
    ResponseTemplate responseTemplate;

    @Secured("ROLE_STARTUP")
    @PostMapping("/startup/product/{productId}")
    public ResponseEntity<Map> uploadProductFile(@PathVariable Long productId,
                                                 @RequestParam(value = "file") MultipartFile file,
                                                 Principal principal) {
        Map response = fileUploadService.uploadProductFile(principal.getName(), productId, file);

        return responseTemplate.controllerHttpRestResponse(response);
    }

    @Secured("ROLE_STARTUP")
    @PostMapping("/startup/credential/{credentialId}")
    public ResponseEntity<Map> uploadCredentialFile(@PathVariable Long credentialId,
                                                 @RequestParam(value = "file") MultipartFile file,
                                                 Principal principal) {
        Map response = fileUploadService.uploadCredentialFile(principal.getName(), credentialId, file);

        return responseTemplate.controllerHttpRestResponse(response);
    }

    @Secured("ROLE_STARTUP")
    @PostMapping("/startup/logo")
    public ResponseEntity<Map> uploadStartupLogoFile(@RequestParam(value = "file") MultipartFile file,
                                                Principal principal) {
        Map response = fileUploadService.uploadStartupLogoFile(principal.getName(), file);

        return responseTemplate.controllerHttpRestResponse(response);
    }

    @Secured("ROLE_INVESTOR")
    @PostMapping("/investor/citizen-id")
    public ResponseEntity<Map> uploadCitizenIdFile(@RequestParam(value = "file") MultipartFile file,
                                                    Principal principal) {
        Map response = fileUploadService.uploadInvestorCitizenId(principal.getName(), file);

        return responseTemplate.controllerHttpRestResponse(response);
    }

    @Secured("ROLE_INVESTOR")
    @PostMapping("/investor/selfie")
    public ResponseEntity<Map> uploadSelfieFile(@RequestParam(value = "file") MultipartFile file,
                                                   Principal principal) {
        Map response = fileUploadService.uploadInvestorSelfie(principal.getName(), file);

        return responseTemplate.controllerHttpRestResponse(response);
    }

    @Secured("ROLE_INVESTOR")
    @PostMapping("/investor/payment-verification/{transactionId}")
    public ResponseEntity<Map> uploadPaymentVerification(
            @PathVariable Long transactionId,
            @RequestParam(value = "file") MultipartFile file,
            Principal principal
    ) {
        Map response = fileUploadService.uploadInvestorPaymentVerification(principal.getName(), transactionId,file);

        return responseTemplate.controllerHttpRestResponse(response);
    }

//    @PostMapping
//    public ResponseEntity<Map> upload(@RequestParam(value = "file") MultipartFile file) throws IOException {
//        Map<String, Object> map = new HashMap<>();
//        try {
//            ObjectMetadata metadata = new ObjectMetadata();
//            metadata.setContentLength(file.getSize());
//
//            String tempFileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
//
//            amazonS3.putObject(bucketName, tempFileName, file.getInputStream(), metadata);
//
//            map.put("status", 200);
//            map.put("url", tempFileName);
//            map.put("size", file.getSize());
//            map.put("content type", file.getContentType());
//            return responseTemplate.controllerHttpRestResponse(map);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new ResponseEntity<Map>(responseTemplate.internalServerError(e),
//                    HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
}
