package com.drewm.dto;

public record AuthRequest(
    String username,
    String password
) {}
