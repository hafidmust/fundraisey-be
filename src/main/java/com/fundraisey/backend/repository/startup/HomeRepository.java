package com.fundraisey.backend.repository.startup;

import com.fundraisey.backend.entity.startup.Startup;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface HomeRepository extends PagingAndSortingRepository<Startup, Long> {
    @Query(value = "SELECT * FROM startup LEFT JOIN WHERE id_user = :userId AND deleted_at IS NULL", nativeQuery = true)
    public List<Startup> getAllProductByStartupId(Long userId);

    // home, loan, history
}
