package com.fundraisey.backend.entity.startup;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fundraisey.backend.entity.DateProps;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "document")
public class Document extends DateProps implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = true, name = "url")
    private String url;

    @ManyToOne(targetEntity = Credential.class)
    @JoinColumn(name = "id_credential", referencedColumnName = "id")
    private Credential credentialDocument;
}
