package com.fundraisey.backend.repository.startup;

import com.fundraisey.backend.entity.startup.Startup;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StartupRepository extends PagingAndSortingRepository<Startup, Long> {
    @Query("SELECT s FROM Startup s WHERE s.user.id = :id AND s.deleted_at IS NULL")
    public Startup getStartupProfileById(@Param("id") Long id);

    Startup findOneByName(String name);
}
