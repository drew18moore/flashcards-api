package com.drewm.repository;

import com.drewm.model.Deck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DeckRepository extends JpaRepository<Deck, Integer> {
    List<Deck> findAllByUserId(Integer userId);

    @Query(value = "SELECT * FROM Deck d WHERE (LOWER(d.name) LIKE LOWER(CONCAT('%', :query, '%')) AND d.is_private = false) OR (LOWER(d.name) LIKE LOWER(CONCAT('%', :query, '%')) AND d.user_id = :userId) LIMIT :limit", nativeQuery = true)
    List<Deck> searchDecks(String query, Integer userId, Integer limit);

    @Query(value = "SELECT * FROM Deck d WHERE d.user_id = :userId AND d.is_private = false", nativeQuery = true)
    List<Deck> findAllPublicDecksByUserId(Integer userId);
}
