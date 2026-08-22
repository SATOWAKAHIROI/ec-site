package com.example.ecsite.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.ecsite.Dto.LoginRequest;
import com.example.ecsite.Dto.LoginResponse;
import com.example.ecsite.domain.User;
import com.example.ecsite.exception.UserNotFoundException;
import com.example.ecsite.repository.UserRepository;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        String loginEmail = loginRequest.email();
        User user = userRepository.findByEmail(loginEmail)
                .orElseThrow(() -> new UserNotFoundException("user not find"));
        String loginPassword = loginRequest.password();

        System.out.println("encoded: " + passwordEncoder.encode("password"));

        if (!passwordEncoder.matches(loginPassword, user.getPassword())) {
            throw new RuntimeException("Password is incorrect");
        }

        String token = jwtService.generateToken(user.getId(), user.getEmail(), user.getRole());

        return new LoginResponse(token);
    }
}
