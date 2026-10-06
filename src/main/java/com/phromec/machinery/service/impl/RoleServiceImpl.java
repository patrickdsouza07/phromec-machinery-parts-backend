package com.phromec.machinery.service.impl;

import com.phromec.machinery.model.Role;
import com.phromec.machinery.repository.RoleRepository;
import com.phromec.machinery.service.RoleService;
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
