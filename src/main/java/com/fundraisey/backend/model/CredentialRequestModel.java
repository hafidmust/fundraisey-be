package com.fundraisey.backend.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class CredentialRequestModel {
    String name;
    String issuingOrganization;
    @JsonFormat(pattern = "dd-MM-yyyy")
    Date issueDate;
    @JsonFormat(pattern = "dd-MM-yyyy")
    Date expirationDate;
    String credentialUrl;
    String credentialId;
    String credentialDescription;
    Long credentialTypeId;
}
