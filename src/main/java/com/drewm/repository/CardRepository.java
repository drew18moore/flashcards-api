package com.drewm.repository;

import com.drewm.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Integer> {
    List<Card> findAllByDeckId(Integer deckId);
    Integer countByDeckId(Integer deckId);

    @Query(value = "SELECT * FROM card WHERE deck_id = :deckId ORDER BY RANDOM() LIMIT :num", nativeQuery = true)
    List<Card> findRandomCardsByDeckId(@Param("deckId") Integer deckId, @Param("num") int num);
}
