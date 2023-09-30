package com.drewm.dto;

import java.time.LocalDateTime;

public record DeckDTO(
        Integer id,
        Integer userId,
        String name,
        boolean isPrivate,
        LocalDateTime createdAt
) {
}
