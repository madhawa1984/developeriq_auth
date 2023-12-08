package com.developeriq.authservice.controller;

import com.developeriq.authservice.Services.AuthService;
import com.developeriq.authservice.dto.AuthRequest;
import com.developeriq.authservice.dto.AuthResponse;
import com.developeriq.authservice.dto.RegisterRequest;
import com.developeriq.authservice.dto.RegisterResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/authenticate")
    public AuthResponse authenticate(@RequestBody AuthRequest authRequest) {
        return authService.authenticate(authRequest);
    }
    @PostMapping("/validateToken")
    public String validateToken() {
        return "Token";
    }

    @PostMapping("/register")
    public RegisterResponse register(@RequestBody RegisterRequest request) {

        return authService.register(request);
    }

    @PostMapping("/current-user")
    public String getCurrent() {
        return "ok";
    }
}
