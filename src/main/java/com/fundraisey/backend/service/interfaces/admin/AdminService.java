package com.fundraisey.backend.service.interfaces.admin;

import com.fundraisey.backend.model.CredentialStatusModel;
import com.fundraisey.backend.model.InvestorVerificationModel;
import com.fundraisey.backend.model.LoanStatusModel;

import java.util.Map;

public interface AdminService {
    public Map getAllUnacceptedLoan(Integer page, Integer size, String sortAttribute, String sortType);
    public Map getUnacceptedLoanById(Long id);
    public Map acceptLoan(LoanStatusModel loanStatusModel);
    public Map rejectLoan(LoanStatusModel loanStatusModel);
    public Map getAllUnacceptedInvestorVerification(Integer page, Integer size, String sortAttribute, String sortType);
    public Map getInvestorVerificationByInvestorId(Long investorId);
    public Map acceptInvestorVerification(InvestorVerificationModel investorVerificationModel);
    public Map getAllPendingCredential(Integer page, Integer size, String sortAttribute, String sortType);
    public Map acceptCredential(CredentialStatusModel credentialStatusModel);
    public Map rejectCredential(CredentialStatusModel credentialStatusModel);
}
