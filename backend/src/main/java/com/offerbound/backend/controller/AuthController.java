package com.offerbound.backend.controller;

import com.offerbound.backend.dto.AuthResponse;
import com.offerbound.backend.dto.LoginRequest;
import com.offerbound.backend.dto.RegisterRequest;
import com.offerbound.backend.entity.User;
import com.offerbound.backend.service.JwtService;
import com.offerbound.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;

    public AuthController(
            UserService userService,
            JwtService jwtService
    ) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public String register(
            @RequestBody RegisterRequest request
    ) {

        return userService.registerUser(request);
    }

    @PostMapping("/login")
    public AuthResponse login(
            @RequestBody LoginRequest request
    ) {

        User user =
                userService.authenticateAndGetUser(
                        request
                );

        String token =
                jwtService.generateToken(
                        user.getEmail()
                );

        return new AuthResponse(
                token,
                "Login Successful",
                user.getId()
        );
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<AuthResponse> handleAuthError(
            RuntimeException ex
    ) {

        return ResponseEntity
                .status(401)
                .body(
                        new AuthResponse(
                                null,
                                ex.getMessage(),
                                null
                        )
                );
    }
}