package com.drewm.controller;

import com.drewm.service.DeckService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/deck")
@RequiredArgsConstructor
public class DeckController {
    private final DeckService deckService;

    @GetMapping
    public ResponseEntity<?> getAllDecksByUser(Authentication authentication) {
        return ResponseEntity.ok(deckService.getAllDecksByUser(authentication));
    }
}
