package com.fundraisey.backend.entity.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fundraisey.backend.entity.DateProps;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "user_detail")
public class UserDetail extends DateProps implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_user", referencedColumnName = "id")
    private User user;

    @Column(length = 100, nullable = false, name = "citizen_id")
    private String citizenID;

    @Column(length = 100, nullable = false, name = "full_name")
    private String fullName;

    @Column(nullable = true, name = "profile_picture")
    private String profilePicture;

    @Column(length = 100, nullable = true, name = "phone_number")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(length = 100, nullable = false, name = "gender")
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(length = 100, nullable = false, name = "date_of_birth")
    private Gender dateOfBirth;
}
