package com.fundraisey.backend.service.implementation;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.fundraisey.backend.entity.auth.User;
import com.fundraisey.backend.entity.investor.Investor;
import com.fundraisey.backend.entity.investor.InvestorVerification;
import com.fundraisey.backend.entity.startup.*;
import com.fundraisey.backend.entity.transaction.Transaction;
import com.fundraisey.backend.repository.auth.UserRepository;
import com.fundraisey.backend.repository.investor.InvestorRepository;
import com.fundraisey.backend.repository.investor.InvestorVerificationRepository;
import com.fundraisey.backend.repository.investor.TransactionRepository;
import com.fundraisey.backend.repository.startup.*;
import com.fundraisey.backend.service.interfaces.FileUploadService;
import com.fundraisey.backend.util.ResponseTemplate;
import org.apache.commons.io.FilenameUtils;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class FileUploadImplementation implements FileUploadService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductPhotoRepository productPhotoRepository;
    @Autowired
    CredentialRepository credentialRepository;
    @Autowired
    DocumentRepository documentRepository;
    @Autowired
    InvestorVerificationRepository investorVerificationRepository;
    @Autowired
    InvestorRepository investorRepository;
    @Autowired
    StartupRepository startupRepository;
    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    ResponseTemplate responseTemplate;

    @Value("${document.bucket-name}")
    private String bucketName;
    @Value("${file_base_url}")
    private String fileBaseUrl;

    @Autowired
    private AmazonS3 amazonS3;

    @Override
    public Map uploadProductFile(String email, Long productId, MultipartFile file) {
        try {
            User user = userRepository.findOneByEmail(email);
            Product product = productRepository.getByProductId(productId);
            if (product == null) return responseTemplate.notFound("Product not found");
            if (user.getId() != product.getStartup().getUser().getId()) return responseTemplate.notAllowed("Not the owner " +
                    "of product");

            String tempFileName = this.upload(file, "product/");

            ProductPhoto productPhoto = new ProductPhoto();
            productPhoto.setProduct(product);
            productPhoto.setUrl(fileBaseUrl + tempFileName);
            ProductPhoto saved = productPhotoRepository.save(productPhoto);

            return responseTemplate.success(saved);
        } catch (FileUploadException e) {
            return responseTemplate.notAllowed(e.getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return responseTemplate.internalServerError(e.getLocalizedMessage());
        }
    }

    @Override
    public Map uploadCredentialFile(String email, Long credentialId, MultipartFile file) {
        try {
            User user = userRepository.findOneByEmail(email);
            Credential credential = credentialRepository.getById(credentialId);
            if (credential == null) return responseTemplate.notFound("Credential not found");
            if (user.getId() != credential.getStartup().getUser().getId()) return responseTemplate.notAllowed("Not the " +
                    "owner of credential");

            String tempFileName = this.upload(file, "credential/");

            Document document = new Document();
            document.setCredential(credential);
            document.setUrl(fileBaseUrl + tempFileName);
            Document saved = documentRepository.save(document);

            return responseTemplate.success(saved);
        } catch (FileUploadException e) {
            return responseTemplate.notAllowed(e.getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return responseTemplate.internalServerError(e.getLocalizedMessage());
        }
    }

    @Override
    public Map uploadStartupLogoFile(String email, MultipartFile file) {
        try {
            Startup startup = startupRepository.getByUserEmail(email);
            if (startup == null) return responseTemplate.notFound("Startup not found");

            String tempFileName = this.upload(file, "logo/");

            startup.setLogo(fileBaseUrl + tempFileName);
            Startup saved = startupRepository.save(startup);

            return responseTemplate.success(saved);
        } catch (FileUploadException e) {
            return responseTemplate.notAllowed(e.getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return responseTemplate.internalServerError(e.getLocalizedMessage());
        }
    }

    @Override
    public Map uploadInvestorCitizenId(String email, MultipartFile file) {
        try {
            User user = userRepository.findOneByEmail(email);
            Investor investor = investorRepository.getByUserId(user.getId());
            InvestorVerification investorVerification = investorVerificationRepository.getByInvestorId(investor.getId());
            if (investorVerification == null) {
                investorVerification = new InvestorVerification();
                investorVerification = investorVerificationRepository.save(investorVerification);
            }

            String tempFileName = this.upload(file, "citizen-id/");

            investorVerification.setCitizenIdPhotoUrl(fileBaseUrl + tempFileName);
            InvestorVerification saved = investorVerificationRepository.save(investorVerification);

            return responseTemplate.success(saved);
        } catch (FileUploadException e) {
            return responseTemplate.notAllowed(e.getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return responseTemplate.internalServerError(e.getLocalizedMessage());
        }
    }

    @Override
    public Map uploadInvestorSelfie(String email, MultipartFile file) {
        try {
            User user = userRepository.findOneByEmail(email);
            Investor investor = investorRepository.getByUserId(user.getId());
            InvestorVerification investorVerification = investorVerificationRepository.getByInvestorId(investor.getId());
            if (investorVerification == null) {
                investorVerification = new InvestorVerification();
                investorVerification = investorVerificationRepository.save(investorVerification);
            }

            String tempFileName = this.upload(file, "product/");

            investorVerification.setSelfiePhotoUrl(fileBaseUrl + tempFileName);
            InvestorVerification saved = investorVerificationRepository.save(investorVerification);

            return responseTemplate.success(saved);
        } catch (FileUploadException e) {
            return responseTemplate.notAllowed(e.getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return responseTemplate.internalServerError(e.getLocalizedMessage());
        }
    }

    @Override
    public Map uploadInvestorPaymentVerification(String email, Long transactionId, MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        try {
            User user = userRepository.findOneByEmail(email);
            Investor investor = investorRepository.getByUserId(user.getId());
            Transaction transaction = transactionRepository.getById(transactionId);
            if (investor.getId() != transaction.getInvestor().getId())
                return responseTemplate.notAllowed("Not the owner of the transaction");

            String tempFileName = this.upload(file, "payment/");

            transaction.setPaymentVerificationUrl(fileBaseUrl + tempFileName);
            Transaction saved = transactionRepository.save(transaction);
            response.put("url", saved.getPaymentVerificationUrl());

            return responseTemplate.success(response);
        } catch (FileUploadException e) {
            return responseTemplate.notAllowed(e.getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return responseTemplate.internalServerError(e.getLocalizedMessage());
        }
    }

    private String upload(MultipartFile file, String folderName) throws IOException {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        if (extension.equals("jpeg") || extension.equals("jpg")) {
            metadata.setContentType("image/jpg");
        } else if (extension.equals("png")) {
            metadata.setContentType("image/png");
        } else if (extension.equals("pdf")) {
            metadata.setContentType("application/pdf");
            metadata.setContentDisposition("inline");
        } else {
            throw new FileUploadException("Can only upload jpeg, jpg, png, and pdf file");
        }

        String tempFileName = folderName + UUID.randomUUID() + "-" + file.getOriginalFilename();

        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, tempFileName, file.getInputStream(),
                metadata);
        amazonS3.putObject(putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead));

        return tempFileName;
    }
}
