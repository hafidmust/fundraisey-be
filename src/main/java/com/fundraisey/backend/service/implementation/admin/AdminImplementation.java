package com.fundraisey.backend.service.implementation.admin;

import com.fundraisey.backend.entity.investor.InvestorVerification;
import com.fundraisey.backend.entity.startup.*;
import com.fundraisey.backend.model.CredentialStatusModel;
import com.fundraisey.backend.model.InvestorVerificationModel;
import com.fundraisey.backend.model.LoanStatusModel;
import com.fundraisey.backend.repository.investor.InvestorVerificationRepository;
import com.fundraisey.backend.repository.startup.CredentialRepository;
import com.fundraisey.backend.repository.startup.LoanRepository;
import com.fundraisey.backend.repository.startup.StartupNotificationRepository;
import com.fundraisey.backend.repository.startup.StartupNotificationTypeRepository;
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
    InvestorVerificationRepository investorVerificationRepository;
    @Autowired
    StartupNotificationTypeRepository startupNotificationTypeRepository;
    @Autowired
    StartupNotificationRepository startupNotificationRepository;
    @Autowired
    CredentialRepository credentialRepository;
    @Autowired
    ResponseTemplate responseTemplate;

    @Override
    public Map getAllUnacceptedLoan(Integer page, Integer size, String sortAttribute, String sortType) {
        Page<Loan> loans;
        Pageable pageable;
        sortAttribute = sortAttribute.equals("") ? "id" : sortAttribute;
        try {
            if ((sortType.equals("desc")) || (sortType.equals("descending"))) {
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
            Loan saved = loanRepository.save(loan);

            StartupNotification notification = new StartupNotification();
            notification.setStartup(saved.getStartup());
            notification.setStartupNotificationType(startupNotificationTypeRepository.findByName("Loan"));
            notification.setMessage("Loan for " + loan.getName() + " has been accepted.");
            notification.setItemId(saved.getId());
            notification.setStatus(StartupNotificationStatus.approved);
            startupNotificationRepository.save(notification);

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
            Loan saved = loanRepository.save(loan);

            StartupNotification notification = new StartupNotification();
            notification.setStartup(saved.getStartup());
            notification.setStartupNotificationType(startupNotificationTypeRepository.findByName("Loan"));
            notification.setMessage("Loan for " + loan.getName() + " has been rejected.");
            notification.setItemId(saved.getId());
            notification.setStatus(StartupNotificationStatus.rejected);
            startupNotificationRepository.save(notification);

            return responseTemplate.success(null);
        } catch (Exception e) {
            e.printStackTrace();
            return responseTemplate.internalServerError(e);
        }
    }

    @Override
    public Map getAllUnacceptedInvestorVerification(Integer page, Integer size, String sortAttribute, String sortType) {
        Page<InvestorVerification> investorVerifications;
        Pageable pageable;
        sortAttribute = sortAttribute.equals("") ? "id" : sortAttribute;
        try {
            if ((sortType.equals("desc")) || (sortType.equals("descending"))) {
                pageable = PageRequest.of(page, size, Sort.by(sortAttribute).descending());
            } else {
                pageable = PageRequest.of(page, size, Sort.by(sortAttribute).ascending());
            }

            investorVerifications = investorVerificationRepository.findByIsVerified(false, pageable);

            return responseTemplate.success(investorVerifications);
        } catch (Exception e) {
            e.printStackTrace();
            return responseTemplate.internalServerError(e);
        }
    }

    @Override
    public Map getInvestorVerificationByInvestorId(Long investorId) {
        try {
            InvestorVerification investorVerification = investorVerificationRepository.getByInvestorId(investorId);

            return responseTemplate.success(investorVerification);
        } catch (Exception e) {
            e.printStackTrace();
            return responseTemplate.internalServerError(e);
        }
    }

    @Override
    public Map acceptInvestorVerification(InvestorVerificationModel investorVerificationModel) {
        try {
            InvestorVerification investorVerification =
                    investorVerificationRepository.getByInvestorId(investorVerificationModel.getInvestorId());
            investorVerification.setVerified(true);
            InvestorVerification saved = investorVerificationRepository.save(investorVerification);

            return responseTemplate.success(saved);
        } catch (Exception e) {
            e.printStackTrace();
            return responseTemplate.internalServerError(e);
        }
    }

    @Override
    public Map getAllPendingCredential(Integer page, Integer size, String sortAttribute, String sortType) {
        Page<Credential> credentials;
        Pageable pageable;
        sortAttribute = sortAttribute.equals("") ? "id" : sortAttribute;
        try {
            if ((sortType.equals("desc")) || (sortType.equals("descending"))) {
                pageable = PageRequest.of(page, size, Sort.by(sortAttribute).descending());
            } else {
                pageable = PageRequest.of(page, size, Sort.by(sortAttribute).ascending());
            }

            credentials = credentialRepository.findByStatus(CredentialStatus.pending, pageable);

            return responseTemplate.success(credentials);
        } catch (Exception e) {
            e.printStackTrace();
            return responseTemplate.internalServerError(e);
        }
    }

    @Override
    public Map getCredentialById(Long id) {
        try {
            Credential credential = credentialRepository.getById(id);
            if (credential == null) return responseTemplate.notFound("Credential not found");
            return responseTemplate.success(credential);
        } catch (Exception e) {
            e.printStackTrace();
            return responseTemplate.internalServerError(e);
        }
    }

    @Override
    public Map acceptCredential(CredentialStatusModel credentialStatusModel) {
        try {
            Credential credential =
                    credentialRepository.getById(credentialStatusModel.getCredentialId());
            if (credential == null) return responseTemplate.notFound("Credential not found");
            if (credential.getStatus() == CredentialStatus.accepted) return responseTemplate.notAllowed("Already accepted");
            credential.setStatus(CredentialStatus.accepted);
            Credential saved = credentialRepository.save(credential);

            StartupNotification notification = new StartupNotification();
            notification.setStartup(saved.getStartup());
            notification.setStartupNotificationType(startupNotificationTypeRepository.findByName("Credential"));
            notification.setMessage("Credential for " + credential.getName() + " has been accepted.");
            notification.setItemId(saved.getId());
            notification.setStatus(StartupNotificationStatus.approved);
            startupNotificationRepository.save(notification);

            return responseTemplate.success(saved);
        } catch (Exception e) {
            e.printStackTrace();
            return responseTemplate.internalServerError(e);
        }
    }

    @Override
    public Map rejectCredential(CredentialStatusModel credentialStatusModel) {
        try {
            Credential credential =
                    credentialRepository.getById(credentialStatusModel.getCredentialId());
            if (credential == null) return responseTemplate.notFound("Credential not found");
            if (credential.getStatus() == CredentialStatus.rejected) return responseTemplate.notAllowed("Already " +
                    "rejected");
            credential.setStatus(CredentialStatus.rejected);
            Credential saved = credentialRepository.save(credential);

            StartupNotification notification = new StartupNotification();
            notification.setStartup(saved.getStartup());
            notification.setStartupNotificationType(startupNotificationTypeRepository.findByName("Credential"));
            notification.setMessage("Credential for " + credential.getName() + " has been rejected.");
            notification.setItemId(saved.getId());
            notification.setStatus(StartupNotificationStatus.rejected);
            startupNotificationRepository.save(notification);

            return responseTemplate.success(saved);
        } catch (Exception e) {
            e.printStackTrace();
            return responseTemplate.internalServerError(e);
        }
    }
}
