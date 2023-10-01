package com.drewm.repository;

import com.drewm.model.Deck;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeckRepository extends JpaRepository<Deck, Integer> {
    List<Deck> findAllByUserId(Integer userId);
}
