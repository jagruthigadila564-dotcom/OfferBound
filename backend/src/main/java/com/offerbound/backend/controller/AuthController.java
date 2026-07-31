package com.offerbound.backend.controller;

import com.offerbound.backend.dto.LoginRequest;
import com.offerbound.backend.dto.RegisterRequest;
import com.offerbound.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // Register API
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {

        return ResponseEntity.ok(userService.registerUser(request));
    }

    // Login API
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {

        return ResponseEntity.ok(userService.loginUser(request));
    }
}