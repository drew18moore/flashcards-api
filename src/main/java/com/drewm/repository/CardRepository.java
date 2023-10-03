package com.drewm.repository;

import com.drewm.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Integer> {
    List<Card> findAllByDeckId(Integer deckId);
}
