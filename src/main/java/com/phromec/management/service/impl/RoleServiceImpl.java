package com.phromec.management.service.impl;

import com.phromec.management.model.Role;
import com.phromec.management.repository.RoleRepository;
import com.phromec.management.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
}
