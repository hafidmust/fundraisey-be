package com.fundraisey.backend.entity.startup;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fundraisey.backend.entity.investor.BankAccount;
import com.fundraisey.backend.entity.transaction.ReturnInstallment;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "payment_invoice")
public class PaymentInvoice implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(targetEntity = ReturnInstallment.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "return_installment_id", referencedColumnName = "id")
    private ReturnInstallment returnInstallment;

    @Column(name = "payment_date")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="dd-MM-yyyy'T'HH:mm:ss.SSSZ", timezone = "Asia/Jakarta")
    @DateTimeFormat(pattern = "dd-MM-yyyy'T'HH:mm:ss.SSSZ")
    private Date paymentDate;

    @Column(name = "amount")
    private Long amount;
}
