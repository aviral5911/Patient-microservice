package com.pm.authservice.service;

import com.pm.authservice.dto.LoginRequestDTO;
import com.pm.authservice.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserService userService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public Optional<String> authenticate(LoginRequestDTO requestDTO) {

        return userService.findByEmail(requestDTO.getEmail())
                .filter(u-> passwordEncoder.matches(requestDTO.getPassword() , u.getPassword()))
                .map(user -> jwtUtil.generateToken(user.getEmail() , user.getRole()));
    }

    public boolean validate(String authHeader) {
        try{
            jwtUtil.validate(authHeader);
            return true;
        }
        catch(RuntimeException e){
            return false;
        }
    }
}
