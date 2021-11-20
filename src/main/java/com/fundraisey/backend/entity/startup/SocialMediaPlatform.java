package com.fundraisey.backend.entity.startup;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fundraisey.backend.entity.DateProps;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "social_media_platform")

// field/atribut = name, logo, website, dateprops
// foreign key social_media

public class SocialMediaPlatform extends DateProps implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = true, name = "name")
    private String name;

    @Column(length = 100, nullable = true, name = "logo")
    private String logo;

    @Column(length = 100, nullable = true, name = "website")
    private String website;

    @JsonIgnore
    @OneToMany(mappedBy = "socialMediaPlatform")
    List<SocialMedia> socialMedia;
}
