package com.fundraisey.backend.repository.investor;

import com.fundraisey.backend.entity.auth.User;
import com.fundraisey.backend.entity.investor.Investor;
import com.fundraisey.backend.entity.transaction.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface TransactionRepository extends PagingAndSortingRepository<Transaction, Long> {
    @Query("SELECT t FROM Transaction t")
    List<Transaction> getAll();

    @Query("SELECT t FROM Transaction t WHERE t.id = :id")
    Transaction getById(@Param("id") Long id);

    @Query("SELECT t FROM Transaction t WHERE t.investor.id = :id")
    List<Transaction> getByInvestorId(@Param("id") Long id);

    Page<Transaction> findByInvestor(Investor investor, Pageable pageable);

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.transactionStatus = 'paid' AND t.loan.id = :id")
    Long sumOfPaidTransactionByLoanId(@Param("id") Long id);
}
