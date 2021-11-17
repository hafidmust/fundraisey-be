package com.fundraisey.backend.repository;

import com.fundraisey.backend.entity.auth.User;
import com.fundraisey.backend.entity.auth.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDetailRepository extends JpaRepository<UserDetail, Long> {
    @Query("SELECT u FROM UserDetail u WHERE u.id = :id")
    UserDetail getById(@Param("id") Long id);

    @Query("SELECT u FROM UserDetail u WHERE u.user.id = :id")
    UserDetail getByUserId(@Param("id") Long id);

    UserDetail findByUser(User user);
}
