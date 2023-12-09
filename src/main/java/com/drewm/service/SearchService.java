package com.drewm.service;

import com.drewm.dto.DeckDTO;
import com.drewm.dto.SearchDTO;
import com.drewm.dto.UserDTO;
import com.drewm.model.User;
import com.drewm.repository.DeckRepository;
import com.drewm.repository.UserRepository;
import com.drewm.utils.DeckDTOMapper;
import com.drewm.utils.UserDTOMapper;
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

    private final UserRepository userRepository;
    private final UserDTOMapper userDTOMapper;

    public SearchDTO search(String query, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<DeckDTO> decks = deckRepository.searchDecks(query, user.getId()).stream().map(deckDTOMapper).collect(Collectors.toList());
        List<UserDTO> users = userRepository.searchUsers(query).stream().map(userDTOMapper).collect(Collectors.toList());
        return new SearchDTO(decks, users);
    }
}
