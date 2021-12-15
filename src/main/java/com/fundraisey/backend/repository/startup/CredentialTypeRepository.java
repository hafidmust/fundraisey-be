package com.fundraisey.backend.repository.startup;

import com.fundraisey.backend.entity.startup.CredentialType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CredentialTypeRepository extends JpaRepository<CredentialType, Long> {
    @Query("SELECT c FROM CredentialType c WHERE id = :id")
    CredentialType getById(@Param("id") Long id);

    CredentialType findOneByName(String name);
}
