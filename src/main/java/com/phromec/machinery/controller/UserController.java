package com.phromec.machinery.controller;

import java.util.List;

import com.phromec.machinery.model.Permission;
import com.phromec.machinery.model.Role;
import com.phromec.machinery.dto.UserResponse;
import com.phromec.machinery.dto.UserCreateRequest;
import com.phromec.machinery.dto.UserUpdateRequest;
import com.phromec.machinery.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@CrossOrigin(origins = "*", maxAge = 3600000)
@RestController
@RequestMapping("/phromecManagement/api/v1/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	private final PermissionService permissionService;
	private final RoleService roleService;

	@GetMapping()
	@PreAuthorize("hasAuthority('PRODUCT_VIEW')")
	public List<UserResponse> getAllUsers(){
		return userService.getAllUsers();
	}

	@GetMapping("/roles")
	@PreAuthorize("hasAuthority('PRODUCT_VIEW')")
	public List<Role> getAllRoles(){
		return roleService.getAllRoles();
	}

	@GetMapping("/roles/permissions")
	@PreAuthorize("hasAuthority('PRODUCT_VIEW')")
	public List<Permission> getAllPermissions(){ return permissionService.getAllPermissions(); }

	@PostMapping
	@PreAuthorize("hasAuthority('PRODUCT_CREATE')")
	public ResponseEntity<UserResponse> createUser(@RequestBody UserCreateRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
	}

	@PutMapping
	@PreAuthorize("hasAuthority('PRODUCT_UPDATE')")
	public ResponseEntity<UserResponse> updateUser(@RequestBody UserUpdateRequest request) {
		return ResponseEntity.ok(userService.updateUser(request));
	}

	@DeleteMapping
	@PreAuthorize("hasAuthority('PRODUCT_DELETE')")
	public ResponseEntity<Void> deleteUser(@RequestParam Integer userId) {
		userService.deleteUser(userId);
		return ResponseEntity.noContent().build();
	}

}
