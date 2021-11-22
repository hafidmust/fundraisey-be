package com.fundraisey.backend.entity.transaction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "payment_agent")
public class PaymentAgent implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = TransactionMethod.class, cascade = CascadeType.ALL)
    private TransactionMethod transactionMethod;

    @JsonIgnore
    @OneToOne(mappedBy = "paymentAgent")
    private Transaction transaction;

    @Column(name = "name")
    private String name;
}
