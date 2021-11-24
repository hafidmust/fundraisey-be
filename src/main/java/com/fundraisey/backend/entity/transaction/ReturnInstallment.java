package com.fundraisey.backend.entity.transaction;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Column(name = "return_period")
    private Integer returnPeriod;

    @Column(name = "total_return_period")
    private Integer totalReturnPeriod;

    @Column(name = "return_date")
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date returnDate;

    @Column(name = "amount")
    private Long amount;

    @Enumerated
    @Column(name = "return_status")
    private ReturnStatus returnStatus;

    @OneToOne(mappedBy = "returnInstallment")
    private ReturnInvoice returnInvoice;
}
