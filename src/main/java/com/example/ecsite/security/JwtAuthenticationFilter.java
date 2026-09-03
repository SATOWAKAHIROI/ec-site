package com.example.ecsite.security;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.ecsite.service.JwtService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    private String extractToken(HttpServletRequest request){
        if(request.getCookies() == null){
            return null;
        }
        for(Cookie cookie: request.getCookies()){
            if("token".equals((cookie.getName()))){
                return cookie.getValue();
            }
        }
        return null;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String token = extractToken(request);

        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try{
            Long userId = jwtService.extractUserId(token);
            String role = jwtService.extractRole(token);
            String email = jwtService.extractEmail(token);

            LoginUser loginUser = new LoginUser(userId, email, role);

            SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(loginUser, null,
                    List.of(authority));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }catch(ExpiredJwtException e){
            ResponseCookie expired = ResponseCookie.from("token", "").httpOnly(true).path("/").maxAge(0).sameSite("Lax").build();
            response.addHeader(HttpHeaders.SET_COOKIE, expired.toString());
        }
        catch(JwtException | IllegalArgumentException e){
            logger.debug("JWTの検証に失敗しました: " + e.getMessage());
        }

        filterChain.doFilter(request, response);

    }
}
