package com.drewm.repository;

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
class DeckRepositoryTest {
    @Autowired
    private DeckRepository deckRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.save(
                new User(1, "test user",  "testuser", "pass123", null)
        );
    }

    @AfterEach
    void tearDown() {
        deckRepository.deleteAll();
    }

    @Test
    void findAllByUserId() {
        Deck deck = Deck.builder()
                .userId(1)
                .name("Deck #1")
                .isPrivate(true)
                .build();
        deckRepository.save(deck);

        List<Deck> decks = deckRepository.findAllByUserId(1);
        assertThat(decks.size()).isEqualTo(1);
        assertThat(decks.get(0)).isEqualTo(deck);
    }

}