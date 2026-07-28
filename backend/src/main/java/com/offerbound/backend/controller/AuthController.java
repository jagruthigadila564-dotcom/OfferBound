package com.offerbound.backend.controller;


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



    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestBody RegisterRequest request
    ){

        String response = userService.registerUser(request);

        return ResponseEntity.ok(response);
    }

}