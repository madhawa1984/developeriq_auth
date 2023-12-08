package com.developeriq.authservice.Services;

import com.developeriq.authservice.dto.AuthRequest;
import com.developeriq.authservice.dto.AuthResponse;
import com.developeriq.authservice.dto.RegisterRequest;
import com.developeriq.authservice.dto.RegisterResponse;
import com.developeriq.authservice.model.Role;
import com.developeriq.authservice.model.UserInfo;
import com.developeriq.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

}
