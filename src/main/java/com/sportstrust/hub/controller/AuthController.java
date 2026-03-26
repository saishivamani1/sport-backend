package com.sportstrust.hub.controller;

import com.sportstrust.hub.dto.ApiResponse;
import com.sportstrust.hub.dto.auth.AuthResponse;
import com.sportstrust.hub.dto.auth.LoginRequest;
import com.sportstrust.hub.dto.auth.RegisterRequest;
import com.sportstrust.hub.entity.User;
import com.sportstrust.hub.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user registration and login")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user (Athlete, Coach, etc.)")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return new ResponseEntity<>(ApiResponse.success(response, "User registered successfully"), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @Operation(summary = "Login user and return JWT tokens")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success(response, "Login successful"));
    }

    @GetMapping("/me")
    @Operation(summary = "Get current authenticated user")
    public ResponseEntity<ApiResponse<User>> getCurrentUser(@AuthenticationPrincipal User user) {
        if (user == null) {
            return new ResponseEntity<>(ApiResponse.error("Unauthorized"), HttpStatus.UNAUTHORIZED);
        }
        return ResponseEntity.ok(ApiResponse.success(user, "User retrieved successfully"));
    }

    // A simple refresh token endpoint could be added here later if we implement a specific refresh logic
}
