package com.phromec.management.service;

import com.phromec.management.exception.RegistrationException;
import com.phromec.management.model.*;
import com.phromec.management.repository.RoleRepository;
import com.phromec.management.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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


}
