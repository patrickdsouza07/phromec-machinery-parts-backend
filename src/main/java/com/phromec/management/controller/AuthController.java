package com.phromec.management.controller;

import com.phromec.management.model.LoginRequest;
import com.phromec.management.model.LoginResponse;
import com.phromec.management.model.RegisterRequest;
import com.phromec.management.model.User;
import com.phromec.management.service.AuthService;
import com.phromec.management.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/phromecManagement/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody RegisterRequest request) {

        User user = userService.registerUser(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of(
                        "message", "User registered successfully",
                        "userId", user.getUserId(),
                        "username", user.getUsername(),
                        "roleName", user.getRole().getRoleName()
                ));
    }
}
