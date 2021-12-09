package com.fundraisey.backend.entity.startup;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "payment_plan")
public class PaymentPlan {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "interest_rate")
    private Float interestRate;

    @Column(name = "month_interval")
    private Integer monthInterval;

    @Column(name = "total_period")
    private Integer totalPeriod;

    @JsonIgnore
    @OneToMany(mappedBy = "paymentPlan")
    private List<Loan> loans;
}
