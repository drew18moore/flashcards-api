package com.drewm.service;

import com.drewm.dto.DeckDTO;
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
public class SearchService {
    private final DeckRepository deckRepository;
    private final DeckDTOMapper deckDTOMapper;

    public List<DeckDTO> search(String query, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return deckRepository.searchDecks(query, user.getId()).stream().map(deckDTOMapper).collect(Collectors.toList());
    }
}
