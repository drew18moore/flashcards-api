package com.drewm.dto;

import java.time.LocalDateTime;

public record UserDTO(
        Integer id,
        String displayName,
        String username,
        LocalDateTime createdAt
) {}
