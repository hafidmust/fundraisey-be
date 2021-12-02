package com.fundraisey.backend.repository.startup;

import com.fundraisey.backend.entity.startup.Credential;
import com.fundraisey.backend.entity.startup.CredentialStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface CredentialRepository extends PagingAndSortingRepository<Credential, Long> {
    Page<Credential> findByStatus(CredentialStatus status, Pageable pageable);

    @Query("SELECT c FROM Credential c WHERE c.id = :id")
    Credential getById(@Param("id") Long id);
}
