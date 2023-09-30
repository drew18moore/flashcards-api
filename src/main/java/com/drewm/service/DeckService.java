package com.drewm.service;

import com.drewm.repository.DeckRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DeckService {
    private DeckRepository deckRepository;

}
