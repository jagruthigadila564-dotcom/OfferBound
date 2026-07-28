package com.offerbound.backend.service;

import com.offerbound.backend.dto.RegisterRequest;
import com.offerbound.backend.entity.User;
import com.offerbound.backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }



    public String registerUser(RegisterRequest request) {


        // Check if email already exists

        if(userRepository.existsByEmail(request.getEmail())){

            return "Email already registered";
        }


        // Create new user

        User user = new User();

        user.setName(request.getName());

        user.setEmail(request.getEmail());


        // Encrypt password

        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );


        // Save user

        userRepository.save(user);


        return "User Registered Successfully";
    }
}