package com.drewm.controller;

import com.drewm.dto.EditDeckRequest;
import com.drewm.dto.NewDeckRequest;
import com.drewm.service.DeckService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/deck")
@RequiredArgsConstructor
public class DeckController {
    private final DeckService deckService;

    @GetMapping
    public ResponseEntity<?> getAllDecksByUser(@RequestParam(required = false) Integer userId, Authentication authentication) {
        return ResponseEntity.ok(deckService.getAllDecksByUser(userId, authentication));
    }

    @PostMapping
    public ResponseEntity<?> createNewDeck(@RequestBody NewDeckRequest request, Authentication authentication) {
        return ResponseEntity.ok(deckService.newDeck(request, authentication));
    }

    @PatchMapping("/{deckId}")
    public ResponseEntity<?> editDeck(@PathVariable Integer deckId, @RequestBody EditDeckRequest request, Authentication authentication) {
        return ResponseEntity.ok(deckService.editDeck(deckId, request, authentication));
    }

    @DeleteMapping("/{deckId}")
    public ResponseEntity<?> deleteDeck(@PathVariable Integer deckId, Authentication authentication) {
        deckService.deleteDeck(deckId, authentication);
        return ResponseEntity.ok("Deck with id: " + deckId + " has been deleted successfully");
    }

    @GetMapping("/{deckId}/cards")
    public ResponseEntity<?> getAllCardsByDeckId(@PathVariable Integer deckId, Authentication authentication) {
        return ResponseEntity.ok(deckService.getAllCardsByDeckId(deckId, authentication));
    }

    @GetMapping("/{deckId}/test")
    public ResponseEntity<?> getTextQuestions(
            @PathVariable Integer deckId,
            @RequestParam Integer numQuestions,
            @RequestParam Boolean trueFalse,
            @RequestParam Boolean multipleChoice,
            @RequestParam Boolean written,
            Authentication authentication
    ) {
        return ResponseEntity.ok(deckService.getTestQuestions(deckId, numQuestions, trueFalse, multipleChoice, written, authentication));
    }

    @GetMapping("/{deckId}/memory")
    public ResponseEntity<?> generateMemoryGame(
            @PathVariable Integer deckId,
            @RequestParam String difficulty,
            Authentication authentication
    ) {
        return ResponseEntity.ok(deckService.generateMemoryGame(deckId, difficulty, authentication));
    }
}
