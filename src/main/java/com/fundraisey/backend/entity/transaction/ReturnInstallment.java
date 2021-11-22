package com.fundraisey.backend.entity.transaction;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "return_installment")
public class ReturnInstallment implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = Transaction.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "transaction_id", referencedColumnName = "id")
    private Transaction transaction;

    @Column(name = "return_period")
    private Integer returnPeriod;

    @Column(name = "total_return_period")
    private Integer totalReturnPeriod;

    @Column(name = "amount")
    private Long amount;

    @Enumerated
    @Column(name = "return_status")
    private ReturnStatus returnStatus;

    @OneToOne(mappedBy = "returnInstallment")
    private ReturnInvoice returnInvoice;
}
