package com.fundraisey.backend.repository.investor;

import com.fundraisey.backend.entity.startup.Payment;
import com.fundraisey.backend.entity.transaction.ReturnInstallment;
import com.fundraisey.backend.entity.transaction.ReturnStatus;
import com.fundraisey.backend.entity.transaction.Transaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReturnInstallmentRepository extends PagingAndSortingRepository<ReturnInstallment, Long> {
    @Query("SELECT SUM(r.amount) FROM ReturnInstallment r WHERE r.transaction.loan.id = :loanId AND r" +
            ".payment.returnPeriod = :period")
    Long getAmountSumByLoanIdAndPeriod(@Param("loanId") Long loanId, @Param("period") Integer period);

//    ReturnInstallment findOneByTransactionAndReturnPeriod(Transaction transaction, Integer returnPeriod);

    ReturnInstallment findOneByTransactionAndPayment(Transaction transaction, Payment payment);

    List<ReturnInstallment> findByTransactionOrderByIdAsc(Transaction transaction);

    @Query("SELECT SUM(r.amount) FROM ReturnInstallment r WHERE r.transaction.investor.id = :investorId")
    Long getAmountSumByInvestorId(@Param("investorId") Long investorId);

    @Query("SELECT r FROM ReturnInstallment r WHERE r.transaction.loan.id = :loanId and r.payment.returnPeriod = :period")
    List<ReturnInstallment> getByLoanIdAndPeriod(@Param("loanId") Long loanId, @Param("period") Integer period);

    @Query("SELECT r FROM ReturnInstallment r WHERE r.transaction.investor.id = :investorId ORDER BY id ASC")
    List<ReturnInstallment> getByTransactionInvestorIdDesc(@Param("investorId") Long investorId);

    @Query("SELECT r FROM ReturnInstallment r WHERE r.id = :id")
    ReturnInstallment getById(@Param("id") Long id);

    @Query("SELECT SUM(r.amount) FROM ReturnInstallment r WHERE r.returnStatus = :returnStatus")
    Long getAmountSumByReturnStatus(ReturnStatus returnStatus);

    @Query("SELECT r FROM ReturnInstallment r WHERE r.transaction.investor.user.email = :email AND r.returnStatus = " +
            "'paid' AND is_withdrawn = false")
    List<ReturnInstallment> getAllPaidAndNotWithdrawnReturnByUserEmail(@Param("email") String email);

    @Query("SELECT SUM(r.amount) FROM ReturnInstallment r WHERE r.transaction.investor.user.email = :email AND r" +
            ".returnStatus = 'paid' AND is_withdrawn = false")
    Long getAmountSumOfAllPaidAndNotWithdrawnReturnByUserEmail(@Param("email") String email);

    @Query("SELECT SUM(r.amount) FROM ReturnInstallment r WHERE r.payment.loan.id = :loanId")
    Long getAmountSumByLoanid(@Param("loanId") Long loanId);
}
