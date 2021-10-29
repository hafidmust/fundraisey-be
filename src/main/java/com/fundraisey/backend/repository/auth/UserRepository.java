package com.fundraisey.backend.repository.auth;

import com.fundraisey.backend.entity.auth.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    @Query("FROM User u WHERE LOWER(u.email) = LOWER(?1)")
    User findOneByEmail(String email);

    @Query("FROM User u WHERE u.otp = ?1")
    User findOneByOTP(String otp);
}

