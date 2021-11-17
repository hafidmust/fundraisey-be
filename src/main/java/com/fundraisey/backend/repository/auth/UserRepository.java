package com.fundraisey.backend.repository.auth;

import com.fundraisey.backend.entity.auth.Role;
import com.fundraisey.backend.entity.auth.User;
import com.fundraisey.backend.entity.auth.UserDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.id = :id")
    User getById(@Param("id") Long id);

    @Query("FROM User u WHERE LOWER(u.email) = LOWER(?1)")
    User findOneByEmail(String email);

    @Query("FROM User u WHERE u.otp = ?1")
    User findOneByOTP(String otp);

    Page<User> findByRolesIn(Collection<Role> names, Pageable pageable);
}

