package com.drewm.dto;

public record RegisterRequest(
    String displayName,
    String username,
    String password
) {}
