package com.fundraisey.backend.entity.transaction;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fundraisey.backend.entity.startup.Payment;
import com.fundraisey.backend.entity.startup.PaymentInvoice;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "return_installment")
public class ReturnInstallment implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(targetEntity = Transaction.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "transaction_id", referencedColumnName = "id")
    private Transaction transaction;

    @Column(name = "amount")
    private Long amount;

    @Column(name = "is_withdrawn")
    private boolean withdrawn = false;

    @Enumerated
    @Column(name = "return_status")
    private ReturnStatus returnStatus;

    @OneToOne(mappedBy = "returnInstallment")
    private ReturnInvoice returnInvoice;

    @OneToOne(mappedBy = "returnInstallment")
    private PaymentInvoice paymentInvoice;

    @OneToOne(targetEntity = Payment.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_id", referencedColumnName = "id")
    @JsonManagedReference
    private Payment payment;
}
