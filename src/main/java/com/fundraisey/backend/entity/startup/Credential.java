package com.fundraisey.backend.entity.startup;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fundraisey.backend.entity.DateProps;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "credential")
public class Credential extends DateProps implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true, name = "credential_url")
    private String credentialUrl;

    @Column(nullable = true, name = "credential_id")
    private String credentialId;

    @Column(length = 100, nullable = true, name = "name")
    private String name;

    @Column(nullable = true, name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(length = 100, nullable = true, name = "issuing_organization")
    private String issuingOrganization;

    @Column(nullable = true, name = "issue_date")
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern="yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date issueDate;

    @Column(length = 100, nullable = true, name = "expiration_date")
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern="yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date expirationDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "status")
    private CredentialStatus status;

    @ManyToOne(targetEntity = Startup.class)
    @JoinColumn(name = "id_startup", referencedColumnName = "id", updatable = false, insertable = true)
    @JsonBackReference
    private Startup startup;

    @ManyToOne(targetEntity = CredentialType.class)
    @JoinColumn(name = "id_credential_type", referencedColumnName = "id")
    private CredentialType credentialType;

    @OneToMany(mappedBy = "credential", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Document> documents;
}
