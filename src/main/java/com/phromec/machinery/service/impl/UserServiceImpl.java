package com.phromec.machinery.service.impl;

import com.phromec.machinery.exception.RegistrationException;
import com.phromec.machinery.dto.RegisterRequest;
import com.phromec.machinery.model.Role;
import com.phromec.machinery.model.User;
import com.phromec.machinery.dto.UserResponse;
import com.phromec.machinery.dto.UserCreateRequest;
import com.phromec.machinery.dto.UserUpdateRequest;
import com.phromec.machinery.repository.RoleRepository;
import com.phromec.machinery.repository.UserRepository;
import com.phromec.machinery.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAllUsers();
    }

    @Transactional
    public User registerUser(RegisterRequest request) {

        System.out.println(request.toString());

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RegistrationException("Username already exists");
        }

        if (request.getEmail() != null &&
                userRepository.existsByEmail(request.getEmail())) {
            throw new RegistrationException("Email already exists");
        }


        Role role = roleRepository.findByRoleName(request.getRoleName())
                .orElseThrow(() ->
                        new RegistrationException("Role not found"));

        User user = new User();
        user.setUsername(request.getUsername());
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPasswordHash(
                passwordEncoder.encode(request.getPassword()));
        user.setRole(role);
        user.setIsActive(true);

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public UserResponse createUser(UserCreateRequest request) {
        validateRequired(request.getFullName(), "Full name");
        validateRequired(request.getEmail(), "Email");
        validateRequired(request.getRole(), "Role");
        validateRequired(request.getTemporaryPassword(), "Temporary password");
        if (request.getStatus() == null) throw new IllegalArgumentException("Status is required");
        if (userRepository.existsByEmail(request.getEmail().trim())) {
            throw new IllegalArgumentException("Email already exists");
        }

        Role role = findRole(request.getRole());
        User user = new User();
        user.setFullName(request.getFullName().trim());
        user.setEmail(request.getEmail().trim());
        user.setRole(role);
        user.setIsActive(request.getStatus());
        user.setUsername(generateUsername(request.getFullName()));
        user.setPasswordHash(passwordEncoder.encode(request.getTemporaryPassword()));
        user.setCreatedAt(LocalDateTime.now());
        return toResponse(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserResponse updateUser(UserUpdateRequest request) {
        if (request.getUserId() == null) throw new IllegalArgumentException("User ID is required");
        validateRequired(request.getFullName(), "Full name");
        validateRequired(request.getEmail(), "Email");
        validateRequired(request.getRole(), "Role");
        if (request.getStatus() == null) throw new IllegalArgumentException("Status is required");
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + request.getUserId()));
        String email = request.getEmail().trim();
        if (userRepository.existsByEmailAndUserIdNot(email, user.getUserId())) {
            throw new IllegalArgumentException("Email already exists");
        }
        user.setFullName(request.getFullName().trim());
        user.setEmail(email);
        user.setRole(findRole(request.getRole()));
        user.setIsActive(request.getStatus());
        return toResponse(userRepository.save(user));
    }

    @Override
    @Transactional
    public void deleteUser(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found: " + userId);
        }
        userRepository.deleteById(userId);
    }

    private Role findRole(String roleName) {
        return roleRepository.findByRoleName(roleName.trim())
                .orElseThrow(() -> new IllegalArgumentException("Role not found: " + roleName));
    }

    private String generateUsername(String fullName) {
        String base = fullName.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]+", "");
        if (base.isBlank()) base = "user";
        base = base.substring(0, Math.min(base.length(), 40));
        String candidate = base;
        int suffix = 1;
        while (userRepository.existsByUsername(candidate)) {
            String ending = Integer.toString(suffix++);
            candidate = base.substring(0, Math.min(base.length(), 50 - ending.length())) + ending;
        }
        return candidate;
    }

    private void validateRequired(String value, String field) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException(field + " is required");
    }

    private UserResponse toResponse(User user) {
        UserResponse response = new UserResponse(user.getUserId(), user.getFullName(), user.getEmail(),
                user.getPhone(), user.getRole().getRoleName(), user.getIsActive());
        response.setUsername(user.getUsername());
        response.setStatus(user.getIsActive());
        return response;
    }


}
