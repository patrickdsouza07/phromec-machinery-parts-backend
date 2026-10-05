package com.phromec.management.service;

import com.phromec.management.exception.RegistrationException;
import com.phromec.management.dto.LoginRequest;
import com.phromec.management.dto.LoginResponse;
import com.phromec.management.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(AuthenticationManager authenticationManager,
                       JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public LoginResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(), request.getPassword()));

            UserDetails principal = (UserDetails) authentication.getPrincipal();
            String token = jwtService.generateToken(principal);

            List<String> roles = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .filter(a -> a.startsWith("ROLE_"))
                    .toList();

            return new LoginResponse(token, principal.getUsername(), roles);
        } catch (AuthenticationException ex) {
            throw new RegistrationException("Invalid username or password");
        }
    }
}
