package com.fundraisey.backend.repository.investor;

import com.fundraisey.backend.entity.transaction.ReturnInvoice;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReturnInvoiceRepository extends PagingAndSortingRepository<ReturnInvoice, Long> {
}
