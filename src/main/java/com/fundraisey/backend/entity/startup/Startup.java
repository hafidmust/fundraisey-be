package com.fundraisey.backend.entity.startup;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fundraisey.backend.entity.DateProps;
import com.fundraisey.backend.entity.auth.User;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "startup")
@Where(clause = "deleted_at is null")
// field table = name, description, logo, phoneNumber, web, address, bornDate
// foreign key userID
public class Startup extends DateProps implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = true, name = "name")
    private String name;

    @Column(columnDefinition = "TEXT", nullable = true, name = "description")
    private String description;

    @Column(nullable = true, name = "logo")
    private String logo;

    @Column(length = 100, nullable = true, name = "phone_number")
    private String phoneNumber;

    @Column(length = 100, nullable = true, name = "web")
    private String web;

    @Column(columnDefinition = "TEXT", nullable = true, name = "address")
    private String address;

    @Column(nullable = true, name = "email")
    private String email;

    @Column(nullable = true, name = "born_date")
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern="yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date foundedDate;

    @Column(nullable = true, name = "linkedin")
    private String linkedin;

    @Column(nullable = true, name = "instagram")
    private String instagram;

    @Column(nullable = true, name = "youtube")
    private String youtube;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_user", referencedColumnName = "id")
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "startup", cascade = CascadeType.ALL)
    private List<Loan> loans;

    @OneToMany(mappedBy = "startup", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<SocialMedia> socialMedias;

    @OneToMany(mappedBy = "startup", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Product> products;

    @OneToMany(mappedBy = "startup", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Credential> credentials;

    @OneToMany(mappedBy = "startup", cascade = CascadeType.ALL)
//    @JsonManagedReference
    @JsonIgnore
    private List<StartupNotification> startupNotifications ;
}
