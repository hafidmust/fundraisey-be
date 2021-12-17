package com.fundraisey.backend.repository.startup;

import com.fundraisey.backend.entity.startup.PlatformFeeInvoice;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PlatformFeeInvoiceRepository extends PagingAndSortingRepository<PlatformFeeInvoice, Long> {
}
