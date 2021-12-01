package com.fundraisey.backend.repository.startup;

import com.fundraisey.backend.entity.startup.StartupNotificationType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StartupNotificationTypeRepository extends JpaRepository<StartupNotificationType, Long> {
    StartupNotificationType findByName(String name);
}
