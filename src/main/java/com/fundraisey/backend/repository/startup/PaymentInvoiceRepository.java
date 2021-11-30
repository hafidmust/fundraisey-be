package com.fundraisey.backend.repository.startup;

import com.fundraisey.backend.entity.startup.PaymentInvoice;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentInvoiceRepository extends PagingAndSortingRepository<PaymentInvoice, Long> {

}
