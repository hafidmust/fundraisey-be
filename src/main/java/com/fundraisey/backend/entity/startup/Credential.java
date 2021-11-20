package com.fundraisey.backend.entity.startup;

import com.fundraisey.backend.entity.DateProps;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
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

    @ManyToOne(targetEntity = Startup.class)
    @JoinColumn(name = "id_startup", referencedColumnName = "id")
    private Startup startup;

    @ManyToOne(targetEntity = CredentialType.class)
    @JoinColumn(name = "id_credential_type", referencedColumnName = "id")
    private CredentialType credentialType;

    @OneToMany(mappedBy = "credentialDocument", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Document> documents;
}
