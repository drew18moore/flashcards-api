package com.drewm.dto;

import java.time.LocalDateTime;

public record CardDTO(
        Integer id,
        Integer userId,
        Integer deckId,
        String frontText,
        String backText,
        LocalDateTime createdAt
) {
}
