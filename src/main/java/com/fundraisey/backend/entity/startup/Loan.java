package com.fundraisey.backend.entity.startup;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fundraisey.backend.entity.DateProps;
import com.fundraisey.backend.entity.transaction.Transaction;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "loan")
public class Loan extends DateProps implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = true, name = "name")
    private String name;

    @Column(nullable = true, name = "description")
    private String description;

    @Column(length = 100, nullable = true, name = "start_date")
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern="dd-MM-yyyy")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date startDate;

    @Column(length = 100, nullable = true, name = "end_date")
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern="dd-MM-yyyy")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date endDate;

    @Column(length = 100, nullable = true, name = "target_value")
    private Long targetValue;

    @Column(length = 100, nullable = true, name = "interest_rate")
    private Float interestRate;

    @Column(length = 100, nullable = true, name = "total_return_period")
    private Integer totalReturnPeriod;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "status")
    private LoanStatus status = LoanStatus.pending;

    @Column(name = "is_withdrawn")
    private boolean withdrawn = false;

    @JsonBackReference
    @ManyToOne(targetEntity = Startup.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_startup", referencedColumnName = "id")
    private Startup startup;

    @ManyToOne(targetEntity = PaymentPlan.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_payment_plan", referencedColumnName = "id")
    private PaymentPlan paymentPlan;

    @JsonIgnore
    @OneToMany(mappedBy = "loan")
    private List<Transaction> transactions;

    @JsonIgnore
    @OneToMany(mappedBy = "loan")
    @JsonManagedReference
    private List<LoanComment> loanComment;

    @OneToOne(mappedBy = "loan")
    private WithdrawalInvoice withdrawalInvoice;
}
