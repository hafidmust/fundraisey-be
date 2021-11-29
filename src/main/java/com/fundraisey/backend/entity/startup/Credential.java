package com.fundraisey.backend.entity.startup;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
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

    @Column(length = 100, nullable = true, name = "credential_photo_url")
    private String credentialPhotoUrl;

    @Column(length = 100, nullable = true, name = "name")
    private String name;

    @Column(length = 200, nullable = true, name = "description")
    private String description;

    @Column(length = 100, nullable = true, name = "issue")
    private String issue;

    @Column(length = 100, nullable = true, name = "issue_date")
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern="dd-MM-yyyy")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date issueDate;

    @Column(length = 100, nullable = true, name = "expiration_date")
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern="dd-MM-yyyy")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date expirationDate;

    @Column(name = "credential_expired")
    private Boolean credentialExpired;

    @ManyToOne(targetEntity = Startup.class)
<<<<<<< Updated upstream
    @JoinColumn(name = "id_startup", referencedColumnName = "id")
    private Startup credentialStartup;
=======
    @JoinColumn(name = "id_startup", referencedColumnName = "id", updatable = false, insertable = true)
    @JsonBackReference
    private Startup startup;
>>>>>>> Stashed changes

    @ManyToOne(targetEntity = CredentialType.class)
    @JoinColumn(name = "id_credential_type", referencedColumnName = "id")
    private CredentialType credentialType;

    @OneToMany(mappedBy = "credentialDocument", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Document> documents;
}
