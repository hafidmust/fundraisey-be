package com.fundraisey.backend.repository.investor;

import com.fundraisey.backend.entity.transaction.PaymentAgent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentAgentRepository extends JpaRepository<PaymentAgent, Long> {
    @Query("SELECT pa FROM PaymentAgent pa")
    List<PaymentAgent> getAll();

    PaymentAgent findOneByName(String name);
}
