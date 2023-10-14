package com.drewm.service;

import com.drewm.config.JwtService;
import com.drewm.repository.UserRepository;
import com.drewm.utils.UserDTOMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private UserService userService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserDTOMapper userDTOMapper;

    private AuthService authService;
    @BeforeEach
    void setUp() {
        authService = new AuthService(
                userRepository,
                passwordEncoder,
                jwtService,
                userService,
                authenticationManager,
                userDTOMapper
        );
    }

    @Test
    void register() {
    }

    @Test
    void login() {
    }

    @Test
    void getUserFromToken() {
    }
}