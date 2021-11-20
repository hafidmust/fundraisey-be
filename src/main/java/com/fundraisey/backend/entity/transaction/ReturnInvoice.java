package com.fundraisey.backend.entity.transaction;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fundraisey.backend.entity.investor.BankAccount;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "return_invoice")
public class ReturnInvoice {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(targetEntity = ReturnInstallment.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "return_installment_id", referencedColumnName = "id")
    private ReturnInstallment returnInstallment;

    @OneToOne(targetEntity = BankAccount.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "bank_account_id", referencedColumnName = "id")
    private BankAccount bankAccount;

    @Column(name = "payment_date")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone = "Asia/Jakarta")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Date paymentDate;

    @Column(name = "amount")
    private Long amount;
}
