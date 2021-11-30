package com.fundraisey.backend.repository.startup;

import com.fundraisey.backend.entity.auth.User;
import com.fundraisey.backend.entity.investor.Investor;
import com.fundraisey.backend.entity.startup.Startup;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.fundraisey.backend.entity.startup.Startup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface StartupRepository extends PagingAndSortingRepository<Startup, Long> {
    @Query("SELECT s FROM Startup s WHERE id = :id")
    Startup getById(@Param("id") Long id);

    Startup findByUser(User user);

    @Query("SELECT s FROM Startup s WHERE s.user.email = :email")
    Startup getByUserEmail(@Param("email") String email);

    @Query("SELECT s FROM Startup s")
    List<Startup> getAll();

    @Query("SELECT s FROM Startup s WHERE s.user.id = :id AND s.deleted_at IS NULL")
    public Startup getStartupProfileById(@Param("id") Long id);

    Startup findOneByName(String name);

    Page<Startup> findByUser(User user, Pageable pageable);
}
