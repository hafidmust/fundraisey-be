package com.fundraisey.backend.repository.startup;

import com.fundraisey.backend.entity.startup.PaymentPlan;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentPlanRepository extends PagingAndSortingRepository<PaymentPlan, Long> {
    PaymentPlan findOneByName(String name);

    @Query("SELECT pp FROM PaymentPlan pp WHERE id = :id")
    PaymentPlan getById(@Param("id") Long id);
}
