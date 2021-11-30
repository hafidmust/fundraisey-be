package com.fundraisey.backend.model.startup;

import com.fundraisey.backend.entity.startup.CredentialType;
import com.fundraisey.backend.entity.startup.Document;
import com.fundraisey.backend.entity.startup.ProductPhoto;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CredentialModel {
    private String id;
    private CredentialType credentialType;
    private String credentialPhotoUrl;
    private String name;
    private String description;
    private String issue;
    private Date issueDate;
    private Date expirationDate;
    private Boolean status;
    private Boolean credentialExpired;
    private List<Document> document;
}
