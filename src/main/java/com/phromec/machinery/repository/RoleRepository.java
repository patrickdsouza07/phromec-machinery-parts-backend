package com.phromec.machinery.repository;

import com.phromec.machinery.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository
        extends JpaRepository<Role, Integer> {
    Optional<Role> findByRoleName(String roleName);
}