package com.developeriq.authservice.controller;

import com.developeriq.authservice.Services.AuthService;
import com.developeriq.authservice.dto.*;
import com.developeriq.authservice.exception.UnAuthorisedException;
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
    public ValidateTokenResponse validateToken(@RequestBody ValidateTokenRequest validateTokenReq)
            throws UnAuthorisedException {
        return authService.validate(validateTokenReq);
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
