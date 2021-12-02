package com.fundraisey.backend.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.fundraisey.backend.service.interfaces.FileUploadService;
import com.fundraisey.backend.util.ResponseTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/v1/upload")
public class FileUploadController {
    @Value("${document.bucket-name}")
    private String bucketName;
    @Autowired
    private AmazonS3 amazonS3;
    @Autowired
    FileUploadService fileUploadService;
    @Autowired
    ResponseTemplate responseTemplate;

    @PostMapping("/startup/product/{productId}")
    public ResponseEntity<Map> uploadProductFile(@PathVariable Long productId,
                                                 @RequestParam(value = "file") MultipartFile file,
                                                 Principal principal) {
        Map response = fileUploadService.uploadProductFile(principal.getName(), productId, file);

        return responseTemplate.controllerHttpRestResponse(response);
    }

    @PostMapping("/startup/credential/{credentialId}")
    public ResponseEntity<Map> uploadCredentialFile(@PathVariable Long credentialId,
                                                 @RequestParam(value = "file") MultipartFile file,
                                                 Principal principal) {
        Map response = fileUploadService.uploadCredentialFile(principal.getName(), credentialId, file);

        return responseTemplate.controllerHttpRestResponse(response);
    }

    @PostMapping("/investor/citizen-id")
    public ResponseEntity<Map> uploadCitizenIdFile(@RequestParam(value = "file") MultipartFile file,
                                                    Principal principal) {
        Map response = fileUploadService.uploadInvestorCitizenId(principal.getName(), file);

        return responseTemplate.controllerHttpRestResponse(response);
    }

    @PostMapping("/investor/selfie")
    public ResponseEntity<Map> uploadSelfieFile(@RequestParam(value = "file") MultipartFile file,
                                                   Principal principal) {
        Map response = fileUploadService.uploadInvestorSelfie(principal.getName(), file);

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
