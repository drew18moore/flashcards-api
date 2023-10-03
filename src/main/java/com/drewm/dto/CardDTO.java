package com.drewm.dto;

import java.time.LocalDateTime;

public record CardDTO(
        Integer id,
        Integer userId,
        Integer deckId,
        String fontText,
        String backText,
        LocalDateTime createdAt
) {
}
