package com.fundraisey.backend.entity.investor;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "investor_verification")
public class InvestorVerification {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(targetEntity = Investor.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "investor_id", referencedColumnName = "id")
    private Investor investor;

    @Column(name = "citizen_id_photo_url")
    private String citizenIdPhotoUrl;

    @Column(name = "selfie_photo_url")
    private String selfiePhotoUrl;

    @Column(name = "selfie_citizen_id_photo_url")
    private String selfieCitizenIdPhotoUrl;

    @Column(name = "is_verified")
    private boolean isVerified;
}
