package com.fundraisey.backend.repository.investor;

import com.fundraisey.backend.entity.investor.BankAccount;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountRepository extends PagingAndSortingRepository<BankAccount, Long> {
}
