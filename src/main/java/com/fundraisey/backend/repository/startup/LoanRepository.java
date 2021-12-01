package com.fundraisey.backend.repository.startup;

import com.fundraisey.backend.entity.auth.User;
import com.fundraisey.backend.entity.startup.Loan;
import com.fundraisey.backend.entity.startup.LoanStatus;
import com.fundraisey.backend.entity.startup.Startup;
import com.fundraisey.backend.entity.transaction.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends PagingAndSortingRepository<Loan, Long> {
    Page<Loan> findAll(Pageable pageable);

    Page<Loan> findByStatus(LoanStatus status, Pageable pageable);

    @Query("SELECT l FROM Loan l WHERE (LOWER(l.name) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(l.startup.name) " +
            "LIKE LOWER(CONCAT('%', :name, '%'))) AND l.status = 'accepted'")
    Page<Loan> getAcceptedLoanByNameContainingOrStartupNameContaining(@Param("name") String name, Pageable pageable);

    Page<Loan> findByStartup(Startup startup, Pageable pageable);

    Page<Loan> findByStartupAndNameContainingIgnoreCase(Startup startup, String name, Pageable pageable);

    @Query("SELECT l FROM Loan l WHERE l.id = :id")
    Loan getById(@Param("id") Long id);

    Loan findByTransactionsIn(List<Transaction> transaction);
}
