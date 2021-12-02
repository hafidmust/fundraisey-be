package com.fundraisey.backend.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class CredentialRequestModel {
    String name;
    String issuingOrganization;
    @JsonFormat(pattern = "yyyy-MM-dd")
    Date issueDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    Date expirationDate;
    String credentialUrl;
    String credentialId;
    String credentialDescription;
    Long credentialTypeId;
}
