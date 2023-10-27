package com.drewm.utils;

import com.drewm.dto.DeckDTO;
import com.drewm.model.Deck;
import com.drewm.repository.CardRepository;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class DeckDTOMapper implements Function<Deck, DeckDTO> {

    private final CardRepository cardRepository; // Inject CardRepository

    public DeckDTOMapper(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Override
    public DeckDTO apply(Deck deck) {
        int numberOfCards = cardRepository.countByDeckId(deck.getId());
        return new DeckDTO(
                deck.getId(),
                deck.getUserId(),
                deck.getName(),
                deck.isPrivate(),
                deck.getCreatedAt(),
                numberOfCards
        );
    }
}
