package com.drewm.controller;

import com.drewm.config.JwtService;
import com.drewm.dto.AuthRequest;
import com.drewm.dto.AuthResponse;
import com.drewm.dto.RegisterRequest;
import com.drewm.service.AuthService;
import com.drewm.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authenticationService;
    private final JwtService jwtService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody AuthRequest request
    ) {
        AuthResponse response = authenticationService.login(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/persist")
    public ResponseEntity<?> persistLogin(HttpServletRequest request) {
        return ResponseEntity.ok(authenticationService.getUserFromToken(request));
    }
}
