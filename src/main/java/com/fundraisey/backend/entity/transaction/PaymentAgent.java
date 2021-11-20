package com.fundraisey.backend.entity.transaction;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "payment_agent")
public class PaymentAgent {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = TransactionMethod.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private TransactionMethod transactionMethod;

    @OneToOne(mappedBy = "paymentAgent")
    private Transaction transaction;

    @Column(name = "name")
    private String name;
}
