package com.fundraisey.backend.repository.startup;

import com.fundraisey.backend.entity.startup.SocialMedia;
import com.fundraisey.backend.entity.startup.SocialMediaPlatform;
import com.fundraisey.backend.entity.startup.Startup;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SocialMediaRepository extends PagingAndSortingRepository<SocialMedia, Long> {
    @Query("SELECT s FROM SocialMedia s WHERE s.startup.id = :id AND s.deleted_at IS NULL")
    public List<SocialMedia> getById(@Param("id") Long id);

    SocialMedia findOneByStartupAndSocialMediaPlatform(Startup startup, SocialMediaPlatform socialMediaPlatform);
}
