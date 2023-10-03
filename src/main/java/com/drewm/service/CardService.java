package com.drewm.service;

import com.drewm.dto.CardDTO;
import com.drewm.dto.NewCardRequest;
import com.drewm.exception.ResourceNotFoundException;
import com.drewm.exception.UnauthorizedException;
import com.drewm.model.Card;
import com.drewm.model.Deck;
import com.drewm.model.User;
import com.drewm.repository.CardRepository;
import com.drewm.repository.DeckRepository;
import com.drewm.utils.CardDTOMapper;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CardService {
    private final CardRepository cardRepository;
    private final CardDTOMapper cardDTOMapper;
    private final DeckRepository deckRepository;

    public CardDTO createNewCardInDeck(NewCardRequest request, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Integer userId = user.getId();
        Integer deckId = request.deckId();
        String frontText = request.frontText();
        String backText = request.backText();

        System.out.println(frontText);

        if (!StringUtils.hasLength(frontText)) {
            throw new IllegalArgumentException("frontText cannot be empty");
        }

        if (!StringUtils.hasLength(backText)) {
            throw new IllegalArgumentException("backText cannot be empty");
        }

        Deck deck = deckRepository.findById(deckId).orElseThrow(() -> new ResourceNotFoundException("Deck not found"));

        if(!deck.getUserId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to create cards in this deck");
        }

        Card newCard = cardRepository.save(new Card(userId, deckId, frontText, backText));
        return cardDTOMapper.apply(newCard);
    }
}
