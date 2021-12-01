package com.fundraisey.backend.repository.startup;

import com.fundraisey.backend.entity.startup.Loan;
import com.fundraisey.backend.entity.startup.WithdrawalInvoice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Repository
public interface WithdrawalInvoiceRepository extends PagingAndSortingRepository<WithdrawalInvoice, Long> {
    WithdrawalInvoice findByLoan(Loan loan);

    @Query("SELECT w FROM WithdrawalInvoice w WHERE w.loan.startup.id = :startupId")
    List<WithdrawalInvoice> getByStartupId(@Param("startupId") Long startupId);
}
