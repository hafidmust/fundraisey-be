package com.fundraisey.backend.entity.startup;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fundraisey.backend.entity.DateProps;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "bank")
public class Bank implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "bank_code")
    private String bankCode;

    @JsonIgnore
    @OneToMany(mappedBy = "bank")
    private List<WithdrawalInvoice> withdrawalInvoices;
}
