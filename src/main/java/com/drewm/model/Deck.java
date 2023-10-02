package com.drewm.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Deck {
    @Id
    @SequenceGenerator(name="deck_id_sequence", sequenceName = "deck_id_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "deck_id_sequence")
    private Integer id;
    @NonNull
    private Integer userId;
    @NonNull
    private String name;
    private boolean isPrivate;
    private LocalDateTime createdAt;

    public Deck(@NonNull Integer userId, @NonNull String name, boolean isPrivate) {
        this.userId = userId;
        this.name = name;
        this.isPrivate = isPrivate;
        this.createdAt = LocalDateTime.now();
    }
}
