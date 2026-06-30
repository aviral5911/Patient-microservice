package com.pm.authservice.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtUtil{
    private final Key secretkey;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        byte[] KeyByte = Base64.getDecoder().decode(secret.getBytes(StandardCharsets.UTF_8));
        this.secretkey = Keys.hmacShaKeyFor(KeyByte);
    }

    public String generateToken(String email, String role) {
        return Jwts.builder()
                .subject(email)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 *10)) // 10 hours
                .signWith(secretkey)
                .compact();
    }
}
