package com.drewm.service;

import com.drewm.config.JwtService;
import com.drewm.dto.AuthResponse;
import com.drewm.dto.RegisterRequest;
import com.drewm.dto.UserDTO;
import com.drewm.model.User;
import com.drewm.repository.UserRepository;
import com.drewm.utils.UserDTOMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
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
        // given
        RegisterRequest request = new RegisterRequest("username", "pass123");
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
        UserDTO userDTO = new UserDTO(1, request.username());

        // when
        when(userRepository.existsByUsername(request.username())).thenReturn(false);
        when(passwordEncoder.encode(request.password())).thenReturn("HashedPassword");
        when(jwtService.generateToken(any(User.class))).thenReturn(token);
        when(userDTOMapper.apply(any(User.class))).thenReturn(userDTO);

        // then
        AuthResponse res = authService.register(request);

        // assert
        assertThat(res.token()).isEqualTo(token);
        assertThat(res.userDTO()).isEqualTo(userDTO);
    }

    @Test
    void login() {
    }

    @Test
    void getUserFromToken() {
    }
}