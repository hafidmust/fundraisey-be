package com.fundraisey.backend.service.interfaces.startup;

import com.fundraisey.backend.model.LoanRequestModel;

import java.util.Map;

public interface LoanService {
    Map insert(String email, LoanRequestModel loanRequestModel);
    Map getAllByEmail(Integer page, Integer size, String sortAttribute, String sortType, String search, String email);
    Map getAllAccepted(Integer page, Integer size, String sortAttribute, String sortType, String search);
    Map getById(Long id);
    Map getPaymentList(Long loanId);
    Map pay(String email, Long loanId, Integer period);
    Map withdraw(String email, Long loanId);
    Map getWithdrawalHistory(String email);
}
