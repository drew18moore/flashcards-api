package com.drewm.utils;

import com.drewm.dto.CardDTO;
import com.drewm.model.Card;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class CardDTOMapper implements Function<Card, CardDTO> {

    @Override
    public CardDTO apply(Card card) {
        return new CardDTO(
                card.getId(),
                card.getUserId(),
                card.getDeckId(),
                card.getFrontText(),
                card.getBackText(),
                card.getCreatedAt()
        );
    }
}
