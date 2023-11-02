package com.drewm.service;

import com.drewm.config.JwtService;
import com.drewm.dto.AuthRequest;
import com.drewm.dto.AuthResponse;
import com.drewm.dto.RegisterRequest;
import com.drewm.dto.UserDTO;
import com.drewm.exception.AuthException;
import com.drewm.model.User;
import com.drewm.repository.UserRepository;
import com.drewm.utils.UserDTOMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
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
        RegisterRequest request = new RegisterRequest("Display Name", "username", "pass123");
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
        UserDTO userDTO = new UserDTO(1, request.displayName(), request.username(), null);

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
    void register_emptyStringUsername() {
        // given
        RegisterRequest request = new RegisterRequest("Display Name", "", "pass123");

        // assert
        assertThrows(IllegalArgumentException.class, () -> authService.register(request));
    }

    @Test
    void register_nullUsername() {
        // given
        RegisterRequest request = new RegisterRequest("Display Name", null, "pass123");

        // assert
        assertThrows(IllegalArgumentException.class, () -> authService.register(request));
    }

    @Test
    void register_springOfSpacesUsername() {
        // given
        RegisterRequest request = new RegisterRequest("Display Name", "  ", "pass123");

        // assert
        assertThrows(IllegalArgumentException.class, () -> authService.register(request));
    }

    @Test
    void register_emptyStringPassword() {
        // given
        RegisterRequest request = new RegisterRequest("Display Name", "username", "");

        // assert
        assertThrows(IllegalArgumentException.class, () -> authService.register(request));
    }

    @Test
    void register_nullPassword() {
        // given
        RegisterRequest request = new RegisterRequest("Display Name", "username", null);

        // assert
        assertThrows(IllegalArgumentException.class, () -> authService.register(request));
    }

    @Test
    void register_usernameAlreadyExists() {
        // given
        RegisterRequest request = new RegisterRequest("Display Name", "username", "pass123");

        // when
        when(userRepository.existsByUsername(request.username())).thenReturn(true);

        // assert
        assertThrows(IllegalArgumentException.class, () -> authService.register(request));
    }

    @Test
    void login() {
        // given
        AuthRequest request = new AuthRequest("username", "pass123");
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
        User user = new User(1, "test user", "testuser", "pass123", null);
        UserDTO userDTO = new UserDTO(user.getId(), user.getDisplayName(), request.username(), null);
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null);

        // when
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtService.generateToken(any(User.class))).thenReturn(token);
        when(userDTOMapper.apply(any(User.class))).thenReturn(userDTO);

        // then
        AuthResponse res = authService.login(request);

        // assert
        assertThat(res.token()).isEqualTo(token);
        assertThat(res.userDTO()).isEqualTo(userDTO);
    }

    @Test
    void getUserFromToken() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        User user = new User(1, "test user", "testUser", "pass123", null);
        UserDTO userDTO = new UserDTO(user.getId(), user.getDisplayName(), user.getUsername(), null);

        // when
        when(request.getHeader("Authorization")).thenReturn("Bearer mockToken");
        when(jwtService.extractUsername("mockToken")).thenReturn("testUser");
        when(userService.getUserByUsername("testUser")).thenReturn(Optional.of(user));
        when(userDTOMapper.apply(user)).thenReturn(userDTO);

        // when
        AuthResponse response = authService.getUserFromToken(request);

        // assert
        assertThat(response.token()).isEqualTo("mockToken");
        assertThat(response.userDTO()).isEqualTo(userDTO);
    }

    @Test
    void getUserFromToken_nullAuthHeader() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);

        // when
        when(request.getHeader("Authorization")).thenReturn(null);

        // assert
        assertThrows(AuthException.class, () -> authService.getUserFromToken(request));
    }

    @Test
    void getUserFromToken_missingBearerToken() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);

        // when
        when(request.getHeader("Authorization")).thenReturn("no token");

        // assert
        assertThrows(AuthException.class, () -> authService.getUserFromToken(request));
    }

    @Test
    void getUserFromToken_nullUsername() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);

        // when
        when(request.getHeader("Authorization")).thenReturn("Bearer mockToken");
        when(jwtService.extractUsername("mockToken")).thenReturn(null);

        // assert
        assertThrows(RuntimeException.class, () -> authService.getUserFromToken(request));
    }
}