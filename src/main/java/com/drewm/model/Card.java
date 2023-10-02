package com.drewm.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Card {
    @Id
    @SequenceGenerator(name="card_id_sequence", sequenceName = "card_id_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "card_id_sequence")
    private Integer id;
    @NonNull
    private Integer userId;
    @NonNull
    private Integer deckId;
    @NonNull
    private String frontText;
    @NonNull
    private String backText;
    private LocalDateTime createdAt;

    public Card(
            @NonNull Integer userId,
            @NonNull Integer deckId,
            @NonNull String frontText,
            @NonNull String backText
    ) {
        this.userId = userId;
        this.deckId = deckId;
        this.frontText = frontText;
        this.backText = backText;
        this.createdAt = LocalDateTime.now();
    }
}
