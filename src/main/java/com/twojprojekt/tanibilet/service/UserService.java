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

    public User register(String username, String rawPassword, Set<String> roles) {
        if(repo.existsByUsername(username)) {
            throw new RuntimeException("Username is already in use");
        }

        User u = new User();
        u.setUsername(username);
        u.setPassword(encoder.encode(rawPassword));
        u.setRoles(roles);
        return repo.save(u);
    }

    public User register(String username, String rawPassword) {
        return register(username, rawPassword, Set.of("ROLE_USER"));
    }

    public boolean existsByUsername(String username) {
        return repo.existsByUsername(username);
    }

}
