package com.drewm.dto;

import java.util.List;

public record SearchDTO(
   List<DeckDTO> decks,
   List<UserDTO> users
) {
}
