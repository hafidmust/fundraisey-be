package com.fundraisey.backend.entity.transaction;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fundraisey.backend.entity.DateProps;
import com.fundraisey.backend.entity.auth.User;
import com.fundraisey.backend.entity.investor.Investor;
import com.fundraisey.backend.entity.startup.Loan;
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
@Table(name = "transaction")
public class Transaction extends DateProps implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = Investor.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "investor_id", referencedColumnName = "id")
    private Investor investor;

    @ManyToOne(targetEntity = Loan.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "loan_id", referencedColumnName = "id")
    private Loan loan;

    @ManyToOne(targetEntity = PaymentAgent.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_agent_id", referencedColumnName = "id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private PaymentAgent paymentAgent;

    @Column(name = "amount")
    private Long amount;

    @Column(name = "account_number")
    private String accountNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_status")
    private TransactionStatus transactionStatus;

    @Column(name = "payment_verification_url")
    private String paymentVerificationUrl;

    @Column(name = "payment_deadline")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone = "Asia/Jakarta")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Date paymentDeadline;

    @OneToMany(mappedBy = "transaction")
    private List<ReturnInstallment> returnInstallments;
}
