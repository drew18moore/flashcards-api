package com.drewm.dto;

public record NewCardRequest(
        Integer deckId,
        String frontText,
        String backText
) {
}
