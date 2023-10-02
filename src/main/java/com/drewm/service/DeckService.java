package com.drewm.service;

import com.drewm.dto.DeckDTO;
import com.drewm.dto.EditDeckRequest;
import com.drewm.dto.NewDeckRequest;
import com.drewm.exception.ResourceNotFoundException;
import com.drewm.exception.UnauthorizedException;
import com.drewm.model.Deck;
import com.drewm.model.User;
import com.drewm.repository.DeckRepository;
import com.drewm.utils.DeckDTOMapper;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DeckService {
    private final DeckRepository deckRepository;
    private final DeckDTOMapper deckDTOMapper;

    public List<DeckDTO> getAllDecksByUser(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Integer userId = user.getId();

        return deckRepository.findAllByUserId(userId).stream().map(deckDTOMapper).collect(Collectors.toList());
    }

    public DeckDTO newDeck(NewDeckRequest request, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Integer userId = user.getId();
        String deckName = request.name();
        Boolean isPrivate = request.isPrivate();

        if (!StringUtils.hasLength(deckName)) {
            throw new IllegalArgumentException("Deck name cannot be empty");
        }

        if (isPrivate == null) {
            throw new IllegalArgumentException("isPrivate cannot be empty");
        }

        Deck newDeck = deckRepository.save(new Deck(userId, deckName, isPrivate));
        return deckDTOMapper.apply(newDeck);
    }

    public DeckDTO editDeck(Integer deckId, EditDeckRequest request, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Integer userId = user.getId();
        String deckName = request.name();
        Boolean isPrivate = request.isPrivate();

        if (deckId == null) {
            throw new IllegalArgumentException("deckId cannot be empty");
        }

        Deck deck = deckRepository.findById(deckId).orElseThrow(() -> new ResourceNotFoundException("Deck not found with ID: " + deckId));

        if (!deck.getUserId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to edit this deck");
        }

        if (deckName != null && !deckName.isEmpty()) {
            deck.setName(deckName);
        }

        if (isPrivate != null) {
            deck.setPrivate(isPrivate);
        }

        deckRepository.save(deck);
        return deckDTOMapper.apply(deck);
    }
}
