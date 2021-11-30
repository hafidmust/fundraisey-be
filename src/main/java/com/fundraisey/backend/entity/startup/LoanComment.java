package com.fundraisey.backend.entity.startup;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fundraisey.backend.entity.DateProps;
import com.fundraisey.backend.entity.auth.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "loan_comment")
public class LoanComment extends DateProps implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = Loan.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "loan_id", referencedColumnName = "id")
    @JsonBackReference
    private Loan loan;

    @ManyToOne(targetEntity = User.class, cascade = CascadeType.ALL)
    private User user;

    @Column(name = "content",columnDefinition = "TEXT")
    private String content;
}
