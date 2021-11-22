package com.fundraisey.backend.repository;

import com.fundraisey.backend.entity.auth.User;
import com.fundraisey.backend.entity.investor.Investor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InvestorRepository extends JpaRepository<Investor, Long> {
    @Query("SELECT u FROM Investor u WHERE u.id = :id")
    Investor getById(@Param("id") Long id);

    @Query("SELECT u FROM Investor u WHERE u.user.id = :id")
    Investor getByUserId(@Param("id") Long id);

    Investor findByUser(User user);
}
