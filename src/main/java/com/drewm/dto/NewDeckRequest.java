package com.drewm.dto;


public record NewDeckRequest(
        String name,
        Boolean isPrivate
) {}
