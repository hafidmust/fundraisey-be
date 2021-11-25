package com.fundraisey.backend.repository.startup;

import com.fundraisey.backend.entity.startup.CredentialType;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CredentialTypeRepository extends PagingAndSortingRepository<CredentialType, Long> {
}
