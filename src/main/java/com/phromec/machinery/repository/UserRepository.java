package com.phromec.machinery.repository;

import com.phromec.machinery.model.User;
import com.phromec.machinery.dto.UserResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository
        extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    @Query("""
        SELECT
            u.userId AS userId,
            u.fullName AS fullName,
            u.email AS email,
            u.phone AS phone,
            r.roleName AS roleName
        FROM User u
        JOIN u.role r
    """)
    List<UserResponse> findAllUsers();
}
