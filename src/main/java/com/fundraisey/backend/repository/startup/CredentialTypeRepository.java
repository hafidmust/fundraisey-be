package com.fundraisey.backend.repository.startup;

import com.fundraisey.backend.entity.startup.CredentialType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CredentialTypeRepository extends JpaRepository<CredentialType, Long> {
}
