package com.fundraisey.backend.entity.startup;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fundraisey.backend.entity.DateProps;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

// atribut/ field table =
// ->name,
// ->description,
// ->productPicture,
// ->startDate, endDate
// ->targetValue
// ->interestRate
// ->totalReturnPeriod
// ->DateProps

// foreign key = startupID
@Getter
@Setter
@Entity
@Table(name = "funding")
public class Funding extends DateProps implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = true, name = "name")
    private String name;

    @Column(length = 200, nullable = true, name = "description")
    private String description;

    @Column(length = 100, nullable = true, name = "start_date")
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern="dd-MM-yyyy")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @Column(length = 100, nullable = true, name = "end_date")
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern="dd-MM-yyyy")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    @Column(length = 100, nullable = true, name = "target_value")
    private int targetValue;

    @Column(length = 100, nullable = true, name = "interest_rate")
    private float interestRate;

    @Column(length = 100, nullable = true, name = "total_return_period")
    private int totalReturnPeriod;

    @ManyToOne(targetEntity = Startup.class)
    @JoinColumn(name = "id_startup", referencedColumnName = "id")
    private Startup fundingStartup;
}
