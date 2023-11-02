package com.drewm.dto;

public record EditUserRequest(
        String displayName,
        String username
) {
}
