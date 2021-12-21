package com.fundraisey.backend.entity.startup;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fundraisey.backend.entity.transaction.ReturnInstallment;
import com.fundraisey.backend.entity.transaction.ReturnStatus;
import com.fundraisey.backend.entity.transaction.Transaction;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "payment")
public class Payment {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(name = "return_period")
    private Integer returnPeriod;

    @Column(name = "return_date")
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date returnDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ReturnStatus status = ReturnStatus.unpaid;

    @OneToMany(mappedBy = "payment")
    @JsonBackReference
    private List<ReturnInstallment> returnInstallment;

    @JsonIgnore
    @ManyToOne(targetEntity = Loan.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "loan_id", referencedColumnName = "id")
    private Loan loan;

    @JsonIgnore
    @OneToOne(mappedBy = "payment")
    private PlatformFeeInvoice platformFeeInvoice;
}
