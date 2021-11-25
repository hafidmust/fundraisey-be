package com.fundraisey.backend.repository.startup;

import com.fundraisey.backend.entity.startup.StartupNotification;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface StartupNotificationRepository extends PagingAndSortingRepository<StartupNotification, Long> {
}
