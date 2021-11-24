package com.fundraisey.backend.repository.investor;

import com.fundraisey.backend.entity.auth.Role;
import com.fundraisey.backend.entity.transaction.TransactionMethod;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionMethodRepository extends PagingAndSortingRepository<TransactionMethod, Long> {
    TransactionMethod findOneByName(String name);
}
