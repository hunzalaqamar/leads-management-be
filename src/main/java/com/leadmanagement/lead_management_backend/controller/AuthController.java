package com.leadmanagement.lead_management_backend.controller;

import com.leadmanagement.lead_management_backend.dto.ApiResponseDTO;
import com.leadmanagement.lead_management_backend.dto.LoginRequestDTO;
import com.leadmanagement.lead_management_backend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO<String>> login(@RequestBody LoginRequestDTO loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwtToken = jwtUtil.generateToken(userDetails.getUsername());

            return ResponseEntity.ok(new ApiResponseDTO<>(true, "Login successful", jwtToken));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body(new ApiResponseDTO<>(false, "Invalid credentials", null));
        }
    }
}
