package com.fundraisey.backend.entity.startup;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "withdrawal_invoice")
public class WithdrawalInvoice {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @OneToOne(targetEntity = Loan.class, cascade = CascadeType.ALL)
    private Loan loan;

    @Column(name = "payment_date", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="dd-MM-yyyy'T'HH:mm:ss.SSSZ", timezone = "Asia/Jakarta")
    @CreationTimestamp
    private Date paymentDate;

    @Column(name = "amount")
    private Long amount;
}
