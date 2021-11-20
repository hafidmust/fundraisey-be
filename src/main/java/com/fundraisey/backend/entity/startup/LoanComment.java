package com.fundraisey.backend.entity.startup;

import com.fundraisey.backend.entity.auth.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "loan_comment")
public class LoanComment {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = Loan.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "loan_id", referencedColumnName = "id")
    private Loan loan;

    @ManyToOne(targetEntity = User.class, cascade = CascadeType.ALL)
    private User user;

    @Column(name = "content",columnDefinition = "TEXT")
    private String content;
}
