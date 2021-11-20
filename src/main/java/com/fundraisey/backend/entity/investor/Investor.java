package com.fundraisey.backend.entity.investor;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fundraisey.backend.entity.DateProps;
import com.fundraisey.backend.entity.auth.Gender;
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
@Table(name = "investor")
public class Investor extends DateProps implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = true, name = "citizen_id")
    private String citizenID;

    @Column(length = 100, nullable = true, name = "full_name")
    private String fullName;

    @Column(nullable = true, name = "profile_picture")
    private String profilePicture;

    @Column(length = 100, nullable = true, name = "phone_number")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(length = 100, nullable = true, name = "gender")
    private Gender gender;

    @Column(length = 100, nullable = true, name = "date_of_birth")
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern="dd-MM-yyyy")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateOfBirth;

    @JsonIgnore
    @OneToOne(targetEntity = User.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_user", referencedColumnName = "id")
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "investor")
    private List<BankAccount> bankAccounts;

    @JsonIgnore
    @OneToOne(mappedBy = "investor")
    private InvestorVerification investorVerification;
}
