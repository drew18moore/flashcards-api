package com.drewm.service;

import com.drewm.dto.DeckDTO;
import com.drewm.dto.NewDeckRequest;
import com.drewm.model.Deck;
import com.drewm.model.User;
import com.drewm.repository.CardRepository;
import com.drewm.repository.DeckRepository;
import com.drewm.utils.CardDTOMapper;
import com.drewm.utils.DeckDTOMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeckServiceTest {

    @Mock
    private DeckRepository deckRepository;
    @Mock
    private DeckDTOMapper deckDTOMapper;
    @Mock
    private CardRepository cardRepository;
    @Mock
    private CardDTOMapper cardDTOMapper;
    private DeckService deckService;

    @BeforeEach
    void setUp() {
        deckService = new DeckService(deckRepository, deckDTOMapper, cardRepository, cardDTOMapper);
    }

    @Test
    void getAllDecksByUser() {
        // given
        User user = new User(1, "test user", "testuser", "pass123");
        Authentication authentication = mock(Authentication.class);

        Deck deck1 = new Deck(1, "Deck #1", true);
        Deck deck2 = new Deck(1, "Deck #2", false);

        DeckDTO deckDTO1 = new DeckDTO(deck1.getId(), 1, deck1.getName(), deck1.isPrivate(), deck1.getCreatedAt());
        DeckDTO deckDTO2 = new DeckDTO(deck2.getId(), 1, deck2.getName(), deck2.isPrivate(), deck2.getCreatedAt());

        // when
        when (authentication.getPrincipal()).thenReturn(user);
        when(deckRepository.findAllByUserId(user.getId())).thenReturn(Arrays.asList(deck1, deck2));
        when(deckDTOMapper.apply(deck1)).thenReturn(deckDTO1);
        when(deckDTOMapper.apply(deck2)).thenReturn(deckDTO2);

        // then
        List<DeckDTO> result = deckService.getAllDecksByUser(authentication);

        // assert
        assertThat(result).isEqualTo(Arrays.asList(deckDTO1, deckDTO2));
    }

    @Test
    void newDeck() {
        // given
        User user = new User(1, "test user", "testuser", "pass123");
        NewDeckRequest request = new NewDeckRequest("New Deck", true);
        Authentication authentication = mock(Authentication.class);
        Deck deck = new Deck(1, "Deck #1", true);

        // when
        when(authentication.getPrincipal()).thenReturn(user);
        when(deckRepository.save(any())).thenReturn(deck);

        DeckDTO mockDeckDTO = new DeckDTO(user.getId(), user.getId(), request.name(), request.isPrivate(), null);
        when(deckDTOMapper.apply(any(Deck.class))).thenReturn(mockDeckDTO);

        // then
        DeckDTO deckDTO = deckService.newDeck(request, authentication);

        // assert
        verify(deckRepository, times(1)).save(any());
        assertThat(request.name()).isEqualTo(deckDTO.name());
        assertThat(request.isPrivate()).isEqualTo(deckDTO.isPrivate());
    }

    @Test
    void newDeck_emptyName() {
        // given
        User user = new User(1, "test user", "testuser", "pass123");
        NewDeckRequest request = new NewDeckRequest("", true);
        Authentication authentication = mock(Authentication.class);
        Deck deck = new Deck(1, "Deck #1", true);

        // when
        when(authentication.getPrincipal()).thenReturn(user);

        // assert
        assertThrows(IllegalArgumentException.class, () -> deckService.newDeck(request, authentication));
    }

    @Test
    void editDeck() {
    }

    @Test
    void deleteDeck() {
    }

    @Test
    @Disabled
    void getAllCardsByDeckId() {
    }
}