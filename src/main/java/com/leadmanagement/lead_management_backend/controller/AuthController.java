package com.leadmanagement.lead_management_backend.controller;

import com.leadmanagement.lead_management_backend.dto.ApiResponseDTO;
import com.leadmanagement.lead_management_backend.dto.LoginRequestDTO;
import com.leadmanagement.lead_management_backend.security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO<String>> login(@RequestBody LoginRequestDTO loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );

            final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());
            final String jwt = jwtUtil.generateToken(userDetails.getUsername());

            ApiResponseDTO<String> response = new ApiResponseDTO<>(true, "Login successful", jwt);
            logger.info("Login response: {}", response);
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            ApiResponseDTO<String> response = new ApiResponseDTO<>(false, "Invalid email or password", null);
            logger.error("Login failed: {}", e.getMessage());
            return ResponseEntity.status(401).body(response);
        }
    }
}