package com.drewm.utils;

import com.drewm.dto.DeckDTO;
import com.drewm.model.Deck;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class DeckDTOMapper implements Function<Deck, DeckDTO> {

    @Override
    public DeckDTO apply(Deck deck) {
        return new DeckDTO(
                deck.getId(),
                deck.getUserId(),
                deck.getName(),
                deck.isPrivate(),
                deck.getCreatedAt()
        );
    }
}
