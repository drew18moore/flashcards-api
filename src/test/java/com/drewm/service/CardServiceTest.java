package com.drewm.service;

import com.drewm.dto.CardDTO;
import com.drewm.dto.NewCardRequest;
import com.drewm.model.Card;
import com.drewm.model.Deck;
import com.drewm.model.User;
import com.drewm.repository.CardRepository;
import com.drewm.repository.DeckRepository;
import com.drewm.utils.CardDTOMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {
    @Mock
    private CardRepository cardRepository;

    @Mock
    private CardDTOMapper cardDTOMapper;

    @Mock
    private DeckRepository deckRepository;

    private CardService cardService;

    @BeforeEach
    void setUp() {
        cardService = new CardService(cardRepository, cardDTOMapper, deckRepository);
    }

    @Test
    void createNewCardInDeck() {
        // given
        User user = new User(1, "test user", "testuser", "pass123");
        Authentication authentication = mock(Authentication.class);
        NewCardRequest request = new NewCardRequest(1, "Front", "Back");
        Deck deck = new Deck(request.deckId(), 1, "Deck #1", true, null);
        Card card = new Card(user.getId(), request.deckId(), request.frontText(), request.backText());
        CardDTO mockCardDTO = new CardDTO(card.getId(), user.getId(), deck.getId(), card.getFrontText(), card.getBackText(), null);

        // when
        when(authentication.getPrincipal()).thenReturn(user);
        when(deckRepository.findById(deck.getId())).thenReturn(Optional.of(deck));
        when(cardRepository.save(any())).thenReturn(card);
        when(cardDTOMapper.apply(any(Card.class))).thenReturn(mockCardDTO);

        // then
        CardDTO cardDTO = cardService.createNewCardInDeck(request, authentication);

        // assert
        verify(cardRepository, times(1)).save(any());
        assertThat(request.deckId()).isEqualTo(cardDTO.deckId());
        assertThat(request.frontText()).isEqualTo(cardDTO.fontText());
        assertThat(request.backText()).isEqualTo(cardDTO.backText());
    }

    @Test
    void createNewCardInDeck_emptyDeckId() {
        // given
        User user = new User(1, "test user", "testuser", "pass123");
        Authentication authentication = mock(Authentication.class);
        NewCardRequest request = new NewCardRequest(null, "Front", "Back");

        // when
        when(authentication.getPrincipal()).thenReturn(user);

        // assert
        assertThrows(IllegalArgumentException.class, () -> cardService.createNewCardInDeck(request, authentication));
    }

    @Test
    void createNewCardInDeck_nullFrontText() {
        // given
        User user = new User(1, "test user", "testuser", "pass123");
        Authentication authentication = mock(Authentication.class);
        NewCardRequest request = new NewCardRequest(1, null, "Back");

        // when
        when(authentication.getPrincipal()).thenReturn(user);

        // assert
        assertThrows(IllegalArgumentException.class, () -> cardService.createNewCardInDeck(request, authentication));
    }

    @Test
    void createNewCardInDeck_emptyFrontTextString() {
        // given
        User user = new User(1, "test user", "testuser", "pass123");
        Authentication authentication = mock(Authentication.class);
        NewCardRequest request = new NewCardRequest(1, "", "Back");

        // when
        when(authentication.getPrincipal()).thenReturn(user);

        // assert
        assertThrows(IllegalArgumentException.class, () -> cardService.createNewCardInDeck(request, authentication));
    }

    @Test
    void editCard() {
    }
}