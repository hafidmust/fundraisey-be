package com.fundraisey.backend.repository.auth;

import com.fundraisey.backend.entity.auth.Client;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ClientRepository extends PagingAndSortingRepository<Client, Long> {

    Client findOneByClientId(String clientId);

}

