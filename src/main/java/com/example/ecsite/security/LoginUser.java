package com.example.ecsite.security;

public record LoginUser(
    Long userId,
    String email,
    String role
) {
    
}
