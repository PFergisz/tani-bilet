package com.twojprojekt.tanibilet.service;

import com.twojprojekt.tanibilet.model.User;
import com.twojprojekt.tanibilet.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {
    private final UserRepository repo;
    private final PasswordEncoder encoder;

    public UserService(UserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    public User register(String username, String rawPassword) {
        if(repo.existsByUsername(username)) {
            throw new RuntimeException("Username is already in use");
        }

        User u = new User();
        u.setUsername(username);
        u.setPassword(encoder.encode(rawPassword));
        u.setRoles(Set.of("ROLE_USER"));
        return repo.save(u);
    }



}
