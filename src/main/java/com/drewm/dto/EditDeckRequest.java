package com.drewm.dto;

public record EditDeckRequest(
        String name,
        Boolean isPrivate
) {
}
