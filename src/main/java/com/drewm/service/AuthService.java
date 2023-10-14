package com.drewm.service;

import com.drewm.config.JwtService;
import com.drewm.dto.AuthRequest;
import com.drewm.dto.AuthResponse;
import com.drewm.dto.RegisterRequest;
import com.drewm.dto.UserDTO;
import com.drewm.exception.AuthException;
import com.drewm.exception.ResourceNotFoundException;
import com.drewm.model.User;
import com.drewm.repository.UserRepository;
import com.drewm.utils.UserDTOMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final UserDTOMapper userDTOMapper;

    public AuthResponse register(RegisterRequest request) {
        if (request.username() == null || request.username().trim().isEmpty() || !StringUtils.hasLength(request.password())) {
            throw new IllegalArgumentException("Username and password cannot be empty");
        }

        if (userRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("Username already exists");
        }
        User user = User.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .build();
        userRepository.save(user);
        String token = jwtService.generateToken(user);
        UserDTO userDTO = userDTOMapper.apply(user);
        return new AuthResponse(token, userDTO);
    }

    public AuthResponse login(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );
        User user = (User) authentication.getPrincipal();
        UserDTO userDTO = userDTOMapper.apply(user);
        String token = jwtService.generateToken(user);
        return new AuthResponse(token, userDTO);
    }

    public AuthResponse getUserFromToken(HttpServletRequest request) throws RuntimeException {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new AuthException("Missing Bearer token in Authorization header");
        }

        String token = authorizationHeader.substring(7);
        String username = jwtService.extractUsername(token);

        if (username == null) {
            throw new RuntimeException("Could not extract username from JWT");
        }

        User user = userService.getUserByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User doesn't exist"));
        UserDTO userDTO = userDTOMapper.apply(user);
        return new AuthResponse(token, userDTO);
    }
}