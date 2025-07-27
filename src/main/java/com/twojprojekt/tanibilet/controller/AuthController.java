package com.twojprojekt.tanibilet.controller;

import com.twojprojekt.tanibilet.dto.*;
import com.twojprojekt.tanibilet.security.JwtTokenProvider;
import com.twojprojekt.tanibilet.service.UserService;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authManager;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(UserService userService, AuthenticationManager authManager, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.authManager = authManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req){
        userService.register(req.username, req.password);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest req){
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.username, req.password)
        );
        String token = jwtTokenProvider.generateToken(req.username);
        return ResponseEntity.ok(new JwtResponse(token));
    }
}
