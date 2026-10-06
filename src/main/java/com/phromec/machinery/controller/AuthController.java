package com.phromec.machinery.controller;

import com.phromec.machinery.dto.LoginRequest;
import com.phromec.machinery.dto.LoginResponse;
import com.phromec.machinery.dto.RegisterRequest;
import com.phromec.machinery.model.User;
import com.phromec.machinery.service.AuthService;
import com.phromec.machinery.service.UserService;
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
