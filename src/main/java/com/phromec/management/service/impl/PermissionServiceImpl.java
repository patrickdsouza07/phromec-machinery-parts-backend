package com.phromec.management.service.impl;

import com.phromec.management.model.Permission;
import com.phromec.management.repository.PermissionRepository;
import com.phromec.management.service.PermissionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {
    private final PermissionRepository permissionRepository;

    public PermissionServiceImpl(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }
}
