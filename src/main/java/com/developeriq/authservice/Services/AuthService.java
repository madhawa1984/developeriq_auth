package com.developeriq.authservice.Services;

import com.developeriq.authservice.dto.*;
import com.developeriq.authservice.exception.UnAuthorisedException;
import com.developeriq.authservice.model.Role;
import com.developeriq.authservice.model.UserInfo;
import com.developeriq.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse authenticate(AuthRequest  authRequest) {
        UserInfo user = userRepository.findByUserName(authRequest.getUserName())
                .orElseThrow();
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUserName(),
                        authRequest.getPwd())
        );
        return AuthResponse.builder()
                .token(jwtService.generateToken(user)).build();
    }

    public RegisterResponse register(RegisterRequest request) {

        UserInfo userInfo = UserInfo.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .userName(request.getUserName())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Optional.ofNullable(request.getRole()).orElse(Role.USER))
                .build();
        userRepository.save(userInfo);
        return RegisterResponse.builder()
                .token(jwtService.generateToken(userInfo))
                .build();

    }

    public ValidateTokenResponse validate(ValidateTokenRequest request) throws UnAuthorisedException {
        return ValidateTokenResponse.builder()
                .token(request.getToken())
                .isValid(userRepository
                .findByUserName(jwtService.extractUserName(request.getToken()))
                .map(userInfo -> jwtService.validateToken(request.getToken(), userInfo))
                .orElseThrow(() -> new UnAuthorisedException("User Not Found")))
                .build();



    }

}
