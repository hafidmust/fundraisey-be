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
@Table(name = "credential_type")
public class CredentialType extends DateProps implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = true, name = "name")
    private String name;

    @OneToMany(mappedBy = "credentialType", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Credential> credentials;
}
