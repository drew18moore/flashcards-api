package com.drewm.controller;

import com.drewm.dto.NewCardRequest;
import com.drewm.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/card")
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;

    @GetMapping("/{deckId}")
    public ResponseEntity<?> getAllCardsByDeckId(@PathVariable Integer deckId, Authentication authentication) {
        return ResponseEntity.ok(cardService.getAllCardsByDeckId(deckId, authentication));
    }

    @PostMapping("/{deckId}")
    public ResponseEntity<?> createNewCardInDeck(@PathVariable Integer deckId, @RequestBody NewCardRequest request, Authentication authentication) {
        return ResponseEntity.ok(cardService.createNewCardInDeck(deckId, request, authentication));
    }
}
