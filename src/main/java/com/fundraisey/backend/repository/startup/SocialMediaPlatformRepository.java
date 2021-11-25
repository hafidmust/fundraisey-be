package com.fundraisey.backend.repository.startup;

import com.fundraisey.backend.entity.startup.SocialMediaPlatform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface SocialMediaPlatformRepository extends JpaRepository<SocialMediaPlatform, Long> {
}
