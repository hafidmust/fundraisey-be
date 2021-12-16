package com.fundraisey.backend.repository.investor;

import com.fundraisey.backend.entity.transaction.ReturnInvoice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReturnInvoiceRepository extends PagingAndSortingRepository<ReturnInvoice, Long> {
    @Query("SELECT SUM(r.amount) FROM ReturnInvoice r WHERE r.returnInstallment.transaction.investor.id = :investorId")
    Long getAmountWithdrawnSumByInvestorId(@Param("investorId") Long investorId);

    @Query("SELECT SUM(r.amount) FROM ReturnInvoice r WHERE r.returnInstallment.transaction.investor.id = :investorId" +
            " AND year(r.paymentDate) = :year AND month(r.paymentDate) = :month")
    Long getAmountWithdrawnSumByYearAndMonth(@Param("investorId") Long investorId, @Param("year") Integer year,
                                             @Param("month") Integer month);
}
