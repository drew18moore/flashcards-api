package com.drewm.service;

import com.drewm.dto.CardDTO;
import com.drewm.dto.EditCardRequest;
import com.drewm.dto.NewCardRequest;
import com.drewm.exception.ResourceNotFoundException;
import com.drewm.exception.UnauthorizedException;
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
    void createNewCardInDeck_nullBackText() {
        // given
        User user = new User(1, "test user", "testuser", "pass123");
        Authentication authentication = mock(Authentication.class);
        NewCardRequest request = new NewCardRequest(1, "Front", null);

        // when
        when(authentication.getPrincipal()).thenReturn(user);

        // assert
        assertThrows(IllegalArgumentException.class, () -> cardService.createNewCardInDeck(request, authentication));
    }

    @Test
    void createNewCardInDeck_emptyBackTextString() {
        // given
        User user = new User(1, "test user", "testuser", "pass123");
        Authentication authentication = mock(Authentication.class);
        NewCardRequest request = new NewCardRequest(1, "Front", "");

        // when
        when(authentication.getPrincipal()).thenReturn(user);

        // assert
        assertThrows(IllegalArgumentException.class, () -> cardService.createNewCardInDeck(request, authentication));
    }

    @Test
    void createNewCardInDeck_unauthorizedUser() {
        // given
        User user = new User(1, "test user", "testuser", "pass123");
        Authentication authentication = mock(Authentication.class);
        NewCardRequest request = new NewCardRequest(1, "Front", "Back");
        Deck deck = new Deck(request.deckId(), 2, "Deck #1", true, null);

        // when
        when(authentication.getPrincipal()).thenReturn(user);
        when(deckRepository.findById(deck.getId())).thenReturn(Optional.of(deck));

        // assert
        assertThrows(UnauthorizedException.class, () -> cardService.createNewCardInDeck(request, authentication));
    }

    @Test
    void editCard() {
        // given
        final int cardId = 1;
        User user = new User(1, "test user", "testuser", "pass123");
        Authentication authentication = mock(Authentication.class);
        EditCardRequest request = new EditCardRequest("New Front", "New Back");

        Deck deck = new Deck(1, user.getId(), "Deck #1", true, null);
        Card existingCard = new Card(user.getId(), 1, "Front", "Back");
        CardDTO mockCardDTO = new CardDTO(existingCard.getId(), user.getId(), deck.getId(), request.frontText(), request.backText(), null);

        // when
        when(authentication.getPrincipal()).thenReturn(user);
        when(cardRepository.findById(cardId)).thenReturn(Optional.of(existingCard));
        when(cardDTOMapper.apply(any(Card.class))).thenReturn(mockCardDTO);

        // then
        CardDTO cardDTO = cardService.editCard(cardId, request, authentication);

        // assert
        verify(cardRepository, times(1)).save(any());
        assertThat(cardDTO.fontText()).isEqualTo(request.frontText());
        assertThat(cardDTO.backText()).isEqualTo(request.backText());
    }

    @Test
    void editCard_nullCardId() {
        // given
        User user = new User(1, "test user", "testuser", "pass123");
        Authentication authentication = mock(Authentication.class);
        EditCardRequest request = new EditCardRequest("New Front", "New Back");

        // when
        when(authentication.getPrincipal()).thenReturn(user);

        // assert
        assertThrows(IllegalArgumentException.class, () -> cardService.editCard(null, request, authentication));
    }

    @Test
    void editCard_cardNotFound() {
        // given
        final int cardId = 1;
        User user = new User(1, "test user", "testuser", "pass123");
        Authentication authentication = mock(Authentication.class);
        EditCardRequest request = new EditCardRequest("New Front", "New Back");

        Deck deck = new Deck(1, user.getId(), "Deck #1", true, null);

        // when
        when(authentication.getPrincipal()).thenReturn(user);
        when(cardRepository.findById(cardId)).thenReturn(Optional.empty());

        // assert
        assertThrows(ResourceNotFoundException.class, () -> cardService.editCard(cardId, request, authentication));
    }

    @Test
    void editCard_unauthorizedUser() {
        // given
        final int cardId = 1;
        User user = new User(1, "test user", "testuser", "pass123");
        Authentication authentication = mock(Authentication.class);
        EditCardRequest request = new EditCardRequest("New Front", "New Back");

        Card existingCard = new Card(2, 1, "Front", "Back");

        // when
        when(authentication.getPrincipal()).thenReturn(user);
        when(cardRepository.findById(cardId)).thenReturn(Optional.of(existingCard));

        // assert
        assertThrows(UnauthorizedException.class, () -> cardService.editCard(cardId, request, authentication));
    }

    @Test
    void deleteCard() {
        // given
        final int cardId = 1;
        User user = new User(1, "test user", "testuser", "pass123");
        Card existingCard = new Card(user.getId(), 1, "Front", "Back");
        Authentication authentication = mock(Authentication.class);

        // when
        when(authentication.getPrincipal()).thenReturn(user);
        when(cardRepository.findById(cardId)).thenReturn(Optional.of(existingCard));

        // then
        cardService.deleteCard(cardId, authentication);

        // assert
        verify(cardRepository, times(1)).deleteById(cardId);
    }

    @Test
    void deleteCard_cardNotFound() {
        // given
        final int cardId = 1;
        User user = new User(1, "test user", "testuser", "pass123");
        Authentication authentication = mock(Authentication.class);

        // when
        when(authentication.getPrincipal()).thenReturn(user);
        when(cardRepository.findById(cardId)).thenReturn(Optional.empty());

        // assert
        assertThrows(ResourceNotFoundException.class, () -> cardService.deleteCard(cardId, authentication));
    }

    @Test
    void deleteCard_unauthorizedUser() {
        // given
        final int cardId = 1;
        User user = new User(1, "test user", "testuser", "pass123");
        Card existingCard = new Card(2, 1, "Front", "Back");
        Authentication authentication = mock(Authentication.class);

        // when
        when(authentication.getPrincipal()).thenReturn(user);
        when(cardRepository.findById(cardId)).thenReturn(Optional.of(existingCard));

        // assert
        assertThrows(UnauthorizedException.class, () -> cardService.deleteCard(cardId, authentication));
    }
}