package com.fundraisey.backend.repository.investor;

import com.fundraisey.backend.entity.investor.InvestorVerification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InvestorVerificationRepository extends PagingAndSortingRepository<InvestorVerification, Long> {
    @Query("SELECT i FROM InvestorVerification i WHERE i.id = :id")
    InvestorVerification getById(@Param("id") Long id);

    @Query("SELECT i FROM InvestorVerification i WHERE i.investor.id = :investorId")
    InvestorVerification getByInvestorId(@Param("investorId") Long investorId);

    Page<InvestorVerification> findByIsVerified(boolean isVerified, Pageable pageable);

    @Query("SELECT i FROM InvestorVerification i WHERE i.status=0 AND i.investor.user.enabled = true")
    Page<InvestorVerification> getByStatusPendingAndUserIsEnabled(Pageable pageable);
}
