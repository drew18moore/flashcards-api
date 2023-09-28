package com.drewm.dto;

public record AuthResponse(
    String token,
    UserDTO userDTO
) {}
