package com.fundraisey.backend.service.interfaces.investor;

import com.fundraisey.backend.model.InvestorModel;
import com.fundraisey.backend.model.UserModel;

import java.util.Map;

public interface InvestorService {
    Map getByUserId(Long id);
    Map getByEmail(String email);
    Map update(InvestorModel investorModel);
    Map updateWithEmail(InvestorModel investorModel);
}
