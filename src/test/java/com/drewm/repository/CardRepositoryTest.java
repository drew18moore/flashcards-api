package com.drewm.repository;

import com.drewm.model.Card;
import com.drewm.model.Deck;
import com.drewm.model.User;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@RequiredArgsConstructor
class CardRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DeckRepository deckRepository;

    @Autowired
    private CardRepository cardRepository;

    @BeforeEach
    void setUp() {
        userRepository.save(
                new User(1, "test user",  "testuser", "pass123")
        );
    }

    @AfterEach
    void tearDown() {
        deckRepository.deleteAll();
    }

    @Test
    void findAllByDeckId() {
        // given
        Deck targetDeck = deckRepository.save(new Deck(1, "Deck 1", true));
        Deck otherDeck = deckRepository.save(new Deck(1, "Deck 2", false));

        System.out.println(targetDeck.getId());
        Card card1 = cardRepository.save(new Card(1, targetDeck.getId(), "Front Text", "Back Text"));
        Card card2 = cardRepository.save(new Card(1, targetDeck.getId(), "Front Text", "Back Text"));
        Card card3 = cardRepository.save(new Card(1, otherDeck.getId(), "Front Text", "Back Text"));

        // then
        List<Card> cards = cardRepository.findAllByDeckId(targetDeck.getId());

        // assert
        assertThat(cards.size()).isEqualTo(2);
        assertThat(cards.get(0)).isEqualTo(card1);
        assertThat(cards.get(1)).isEqualTo(card2);
    }
}