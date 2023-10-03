package com.drewm.dto;

public record NewCardRequest(
        String frontText,
        String backText
) {
}
