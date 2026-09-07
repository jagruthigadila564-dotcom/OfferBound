package com.offerbound.backend.security;

import com.offerbound.backend.service.CustomUserDetailsService;
import com.offerbound.backend.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter
        extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            CustomUserDetailsService userDetailsService
    ) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String requestUri = request.getRequestURI();

        System.out.println();
        System.out.println("========== JWT FILTER ==========");
        System.out.println(
                "Request: "
                        + request.getMethod()
                        + " "
                        + requestUri
        );

        String authHeader =
                request.getHeader("Authorization");

        if (authHeader == null || authHeader.isBlank()) {

            System.out.println(
                    "JWT: Authorization header is MISSING"
            );

            filterChain.doFilter(request, response);
            return;
        }

        System.out.println(
                "JWT: Authorization header found"
        );

        if (!authHeader.startsWith("Bearer ")) {

            System.out.println(
                    "JWT: Authorization header does NOT start with Bearer"
            );

            filterChain.doFilter(request, response);
            return;
        }

        String jwt =
                authHeader.substring(7).trim();

        if (jwt.isEmpty()) {

            System.out.println(
                    "JWT: Bearer token is EMPTY"
            );

            filterChain.doFilter(request, response);
            return;
        }

        System.out.println(
                "JWT: Bearer token received"
        );

        try {

            String email =
                    jwtService.extractUsername(jwt);

            System.out.println(
                    "JWT: Extracted email = "
                            + email
            );

            if (email == null || email.isBlank()) {

                System.out.println(
                        "JWT: Email extracted from token is EMPTY"
                );

                filterChain.doFilter(request, response);
                return;
            }

            if (SecurityContextHolder
                    .getContext()
                    .getAuthentication() != null) {

                System.out.println(
                        "JWT: Authentication already exists"
                );

                filterChain.doFilter(request, response);
                return;
            }

            UserDetails userDetails =
                    userDetailsService
                            .loadUserByUsername(email);

            System.out.println(
                    "JWT: User loaded successfully = "
                            + userDetails.getUsername()
            );

            boolean valid =
                    jwtService.isTokenValid(
                            jwt,
                            userDetails.getUsername()
                    );

            System.out.println(
                    "JWT: Token valid = "
                            + valid
            );

            if (valid) {

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authentication.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(
                                authentication
                        );

                System.out.println(
                        "JWT: AUTHENTICATION SUCCESSFUL"
                );

            } else {

                System.out.println(
                        "JWT: TOKEN IS INVALID"
                );
            }

        } catch (Exception e) {

            System.out.println(
                    "JWT: AUTHENTICATION FAILED"
            );

            System.out.println(
                    "JWT Error Type: "
                            + e.getClass().getName()
            );

            System.out.println(
                    "JWT Error Message: "
                            + e.getMessage()
            );
        }

        System.out.println(
                "JWT: Continuing filter chain"
        );

        filterChain.doFilter(request, response);

        System.out.println(
                "JWT: Response status = "
                        + response.getStatus()
        );

        System.out.println(
                "================================"
        );
        System.out.println();
    }
}