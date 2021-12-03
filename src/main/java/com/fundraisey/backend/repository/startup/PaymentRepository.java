package com.fundraisey.backend.repository.startup;

import com.fundraisey.backend.entity.startup.Loan;
import com.fundraisey.backend.entity.startup.Payment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends PagingAndSortingRepository<Payment, Long> {
    @Query("SELECT p FROM Payment p WHERE p.loan.id = :loanId")
    List<Payment> getByLoanId(@Param("loanId") Long loanId);

    @Query("SELECT p FROM Payment p WHERE p.loan.id = :loanId AND p.returnPeriod = :period")
    Payment getByLoanIdAndPeriod(@Param("loanId") Long loanId, @Param("period") Integer period);
}
