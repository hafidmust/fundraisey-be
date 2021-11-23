package com.fundraisey.backend.repository.startup;

import com.fundraisey.backend.entity.auth.User;
import com.fundraisey.backend.entity.investor.Investor;
import com.fundraisey.backend.entity.startup.Startup;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StartupRepository extends PagingAndSortingRepository<Startup, Long> {
    @Query("SELECT s FROM Startup s WHERE id = :id")
    Startup getById(@Param("id") Long id);

    Startup findByUser(User user);
}
