package com.fundraisey.backend.service.implementation.admin;

import com.fundraisey.backend.entity.startup.Loan;
import com.fundraisey.backend.entity.startup.LoanStatus;
import com.fundraisey.backend.model.LoanStatusModel;
import com.fundraisey.backend.repository.startup.LoanRepository;
import com.fundraisey.backend.service.interfaces.admin.AdminService;
import com.fundraisey.backend.util.ResponseTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AdminImplementation implements AdminService {
    @Autowired
    LoanRepository loanRepository;
    @Autowired
    ResponseTemplate responseTemplate;
    @Override
    public Map getAllUnacceptedLoan(Integer page, Integer size, String sortAttribute, String sortType) {
        Page<Loan> loans;
        Pageable pageable;
        sortAttribute = sortAttribute.equals("") ? "id" : sortAttribute;
        try {
            if ((sortType == "desc") || (sortType == "descending")) {
                pageable = PageRequest.of(page, size, Sort.by(sortAttribute).descending());
            } else {
                pageable = PageRequest.of(page, size, Sort.by(sortAttribute).ascending());
            }

            loans = loanRepository.findByStatus(LoanStatus.pending, pageable);

            return responseTemplate.success(loans);
        } catch (Exception e) {
            e.printStackTrace();
            return responseTemplate.internalServerError(e);
        }
    }
    @Override
    public Map getUnacceptedLoanById(Long id) {
        try {
            Loan loan = loanRepository.getById(id);
            if (loan.getStatus() != LoanStatus.pending) return responseTemplate.notAllowed("Loan status not pending");
            return responseTemplate.success(loan);
        } catch (Exception e) {
            e.printStackTrace();
            return responseTemplate.internalServerError(e);
        }
    }
    @Override
    public Map acceptLoan(LoanStatusModel loanStatusModel) {
        try {
            Loan loan = loanRepository.getById(loanStatusModel.getLoanId());
            if (loan == null) return responseTemplate.notFound("Loan not found");
            if (loan.getStatus() == LoanStatus.accepted) return responseTemplate.notAllowed("Already accepted");
            loan.setStatus(LoanStatus.accepted);
            loanRepository.save(loan);
            return responseTemplate.success(null);
        } catch (Exception e) {
            e.printStackTrace();
            return responseTemplate.internalServerError(e);
        }
    }
    @Override
    public Map rejectLoan(LoanStatusModel loanStatusModel) {
        try {
            Loan loan = loanRepository.getById(loanStatusModel.getLoanId());
            if (loan == null) return responseTemplate.notFound("Loan not found");
            if (loan.getStatus() == LoanStatus.rejected) return responseTemplate.notAllowed("Already rejected");
            loan.setStatus(LoanStatus.rejected);
            loanRepository.save(loan);
            return responseTemplate.success(null);
        } catch (Exception e) {
            e.printStackTrace();
            return responseTemplate.internalServerError(e);
        }
    }
}
