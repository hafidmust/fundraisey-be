package com.fundraisey.backend.repository.startup;

import com.fundraisey.backend.entity.startup.Credential;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CredentialRepository extends PagingAndSortingRepository<Credential, Long> {
}
