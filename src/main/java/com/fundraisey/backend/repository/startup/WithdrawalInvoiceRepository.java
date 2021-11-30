package com.fundraisey.backend.repository.startup;

import com.fundraisey.backend.entity.startup.Loan;
import com.fundraisey.backend.entity.startup.WithdrawalInvoice;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WithdrawalInvoiceRepository extends PagingAndSortingRepository<WithdrawalInvoice, Long> {
    WithdrawalInvoice findByLoan(Loan loan);
}
