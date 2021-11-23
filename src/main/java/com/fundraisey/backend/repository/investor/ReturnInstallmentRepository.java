package com.fundraisey.backend.repository.investor;

import com.fundraisey.backend.entity.transaction.ReturnInstallment;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReturnInstallmentRepository extends PagingAndSortingRepository<ReturnInstallment, Long> {
}
