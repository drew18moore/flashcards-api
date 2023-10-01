package com.drewm.service;

import com.drewm.dto.DeckDTO;
import com.drewm.model.Deck;
import com.drewm.model.User;
import com.drewm.repository.DeckRepository;
import com.drewm.utils.DeckDTOMapper;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

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
        System.out.println(user);

        return deckRepository.findAllByUserId(userId).stream().map(deckDTOMapper).collect(Collectors.toList());
    }
}
