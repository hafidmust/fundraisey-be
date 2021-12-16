package com.fundraisey.backend.repository.startup;

import com.fundraisey.backend.entity.startup.Bank;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankRepository extends PagingAndSortingRepository<Bank, Long> {
    @Query("SELECT b FROM Bank b WHERE b.id = :id")
    Bank getById(@Param("id") Long id);

    Bank findOneByName(String name);

    List<Bank> findAll();
}
