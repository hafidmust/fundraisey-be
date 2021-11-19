package com.fundraisey.backend.entity.startup;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fundraisey.backend.entity.DateProps;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "social_media")

// field table = url, bornDate, dateProps
// foreign key = startupID, socialMediaPlatformID
public class SocialMedia extends DateProps implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = SocialMediaPlatform.class)
    @JoinColumn(name = "id_social_media_platform", referencedColumnName = "id")
    private SocialMediaPlatform socialMediaPlatform;

    @ManyToOne(targetEntity = Startup.class)
    @JoinColumn(name = "id_startup", referencedColumnName = "id")
    private Startup socialMediaStartup;

    @Column(length = 100, nullable = true, name = "url")
    private String url;
}
