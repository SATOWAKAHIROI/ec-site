package com.example.ecsite.service;

import java.util.Date;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    private final String secretKey = "my-super-secret-key-my-super-secret-key";

    public String generateToken(Long userId, String email, String role) {
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("email", email)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    public Long extractUserId(String token) {
        String subject = extractAll(token).getSubject();

        return Long.valueOf(subject);
    }

    public String extractRole(String token) {
        String role = extractAll(token).get("role", String.class);

        return role;
    }

    public String extractEmail(String token){
        String email = extractAll(token).get("email", String.class);
        return email;
    }

    private Claims extractAll(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
