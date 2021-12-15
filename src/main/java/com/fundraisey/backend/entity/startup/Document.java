package com.fundraisey.backend.entity.startup;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fundraisey.backend.entity.DateProps;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "document")
@Where(clause = "deleted_at is null")
public class Document extends DateProps implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true, name = "url")
    private String url;

    @ManyToOne(targetEntity = Credential.class)
    @JoinColumn(name = "id_credential", referencedColumnName = "id")
    @JsonBackReference
    private Credential credential;
}
