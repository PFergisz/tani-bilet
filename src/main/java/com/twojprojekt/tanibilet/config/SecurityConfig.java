package com.twojprojekt.tanibilet.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

//@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/ping", "/smoke", "/api/**").permitAll() // <-- zezwól na /ping
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults()); // pozwól na podstawowe logowanie
        return http.build();
    }
}
