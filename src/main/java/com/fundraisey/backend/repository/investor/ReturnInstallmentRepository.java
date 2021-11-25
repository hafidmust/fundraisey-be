package com.fundraisey.backend.repository.investor;

import com.fundraisey.backend.entity.transaction.ReturnInstallment;
import com.fundraisey.backend.entity.transaction.Transaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReturnInstallmentRepository extends PagingAndSortingRepository<ReturnInstallment, Long> {
    @Query("SELECT SUM(r.amount) FROM ReturnInstallment r WHERE r.transaction.loan.id = :loanId and r.returnPeriod = " +
            ":period")
    Long getAmountSumByLoanIdAndPeriod(@Param("loanId") Long loanId, @Param("period") Integer period);

    ReturnInstallment findOneByTransactionAndReturnPeriod(Transaction transaction, Integer returnPeriod);

    @Query("SELECT r FROM ReturnInstallment r WHERE r.transaction.loan.id = :loanId and r.returnPeriod = :period")
    List<ReturnInstallment> getAllByLoanIdAndPeriod(@Param("loanId") Long loanId, @Param("period") Integer period);
}
