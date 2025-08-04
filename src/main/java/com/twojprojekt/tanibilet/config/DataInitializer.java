package com.twojprojekt.tanibilet.config;

import com.twojprojekt.tanibilet.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {
    private final UserService userService;

    public DataInitializer(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) {
        if(!userService.existsByUsername("admin")) {
            userService.register("admin", "admin123", Set.of("ROLE_ADMIN", "ROLE_USER"));
            System.out.println("-> Utworzono domyślnego admina");
        }
    }
}
