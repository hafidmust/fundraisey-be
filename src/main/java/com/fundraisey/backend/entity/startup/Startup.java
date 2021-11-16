package com.fundraisey.backend.entity.startup;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fundraisey.backend.entity.DateProps;
import com.fundraisey.backend.entity.auth.User;
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
@Table(name = "startup")
// field table = name, description, logo, phoneNumber, web, address, bornDate
// foreign key userID
public class Startup extends DateProps implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = true, name = "name")
    private String name;

    @Column(length = 100, nullable = true, name = "description")
    private String description;

    @Column(length = 100, nullable = true, name = "logo")
    private String logo;

    @Column(length = 100, nullable = true, name = "phone_number")
    private String phoneNumber;

    @Column(length = 100, nullable = true, name = "web")
    private String web;

    @Column(length = 100, nullable = true, name = "address")
    private String address;

    @Column(length = 100, nullable = true, name = "born_date")
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern="dd-MM-yyyy")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date bornDate;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_user", referencedColumnName = "id")
    private User user;

    @OneToOne(mappedBy = "socialMediaStartup", cascade = CascadeType.ALL)
    private SocialMedia socialMedia;

    @OneToMany(mappedBy = "projectStartup", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Project> projects;
}
