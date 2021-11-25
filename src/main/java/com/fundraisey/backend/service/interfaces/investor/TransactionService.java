package com.fundraisey.backend.service.interfaces.investor;

import com.fundraisey.backend.entity.transaction.Transaction;
import com.fundraisey.backend.model.TransactionRequestModel;

import java.util.Map;

public interface TransactionService {
    Map insert(String email, TransactionRequestModel transactionRequestModel);
    Map update(Transaction transaction);
    Map getByEmail(Integer page, Integer size, String sortAttribute, String sortType, String email);
    Map getByUserId(Integer page, Integer size, String sortAttribute, String sortType, Long userId);
    Map getAll();
    Map getById(Long id);
    Map pay(String email, TransactionRequestModel transactionRequestModel);
}
