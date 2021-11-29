package com.fundraisey.backend.repository.startup;

import com.fundraisey.backend.entity.auth.User;
import com.fundraisey.backend.entity.startup.StartupNotification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface StartupNotificationRepository extends PagingAndSortingRepository<StartupNotification, Long> {
    @Query("SELECT s FROM StartupNotification s WHERE s.startup.user.id = :user.id")
    Page<StartupNotification> findByUser(User user, Pageable pageable);
}
