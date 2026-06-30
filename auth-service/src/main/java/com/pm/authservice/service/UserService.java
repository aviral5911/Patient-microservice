package com.pm.authservice.service;

import com.pm.authservice.model.User;
import com.pm.authservice.repo.UserRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepo repo;

    public UserService(UserRepo repo) {
        this.repo = repo;
    }

    public Optional<User> findByEmail(String email){
        return repo.findByEmail(email);
    }
}
