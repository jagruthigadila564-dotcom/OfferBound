package com.offerbound.backend.controller;

import com.offerbound.backend.dto.AuthResponse;
import com.offerbound.backend.dto.LoginRequest;
import com.offerbound.backend.dto.RegisterRequest;
import com.offerbound.backend.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // Register
    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {

        return userService.registerUser(request);
    }

    // Login
    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {

        String token = userService.loginUser(request);

        return new AuthResponse(
                token,
                "Login Successful"
        );
    }

}