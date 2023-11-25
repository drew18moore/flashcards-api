package com.drewm.service;

import com.drewm.dto.*;
import com.drewm.exception.ResourceNotFoundException;
import com.drewm.exception.UnauthorizedException;
import com.drewm.model.Card;
import com.drewm.model.Deck;
import com.drewm.model.User;
import com.drewm.repository.CardRepository;
import com.drewm.repository.DeckRepository;
import com.drewm.utils.CardDTOMapper;
import com.drewm.utils.DeckDTOMapper;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DeckService {
    private final DeckRepository deckRepository;
    private final DeckDTOMapper deckDTOMapper;
    private final CardRepository cardRepository;
    private final CardDTOMapper cardDTOMapper;

    public List<DeckDTO> getAllDecksByUser(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Integer userId = user.getId();

        return deckRepository.findAllByUserId(userId).stream().map(deckDTOMapper).collect(Collectors.toList());
    }

    public DeckDTO newDeck(NewDeckRequest request, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Integer userId = user.getId();
        String deckName = request.name();
        Boolean isPrivate = request.isPrivate();

        if (!StringUtils.hasLength(deckName)) {
            throw new IllegalArgumentException("Deck name cannot be empty");
        }

        if (isPrivate == null) {
            throw new IllegalArgumentException("isPrivate cannot be empty");
        }

        Deck newDeck = deckRepository.save(new Deck(userId, deckName, isPrivate));
        return deckDTOMapper.apply(newDeck);
    }

    public DeckDTO editDeck(Integer deckId, EditDeckRequest request, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Integer userId = user.getId();
        String deckName = request.name();
        Boolean isPrivate = request.isPrivate();

        if (deckId == null) {
            throw new IllegalArgumentException("deckId cannot be empty");
        }

        Deck deck = deckRepository.findById(deckId).orElseThrow(() -> new ResourceNotFoundException("Deck not found with ID: " + deckId));

        if (!deck.getUserId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to edit this deck");
        }

        if (deckName != null && !deckName.isEmpty()) {
            deck.setName(deckName);
        }

        if (isPrivate != null) {
            deck.setPrivate(isPrivate);
        }

        deckRepository.save(deck);
        return deckDTOMapper.apply(deck);
    }

    public void deleteDeck(Integer deckId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Integer userId = user.getId();

        Deck deck = deckRepository.findById(deckId).orElseThrow(() -> new ResourceNotFoundException("Deck not found"));

        if (!deck.getUserId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to delete this deck");
        }

        deckRepository.deleteById(deckId);
    }

    public List<CardDTO> getAllCardsByDeckId(Integer deckId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Integer userId = user.getId();

        Deck deck = deckRepository.findById(deckId).orElseThrow(() -> new ResourceNotFoundException("Deck not found"));

        if(deck.isPrivate() && !deck.getUserId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to view cards from this deck");
        }

        return cardRepository.findAllByDeckId(deckId).stream().map(cardDTOMapper).collect(Collectors.toList());
    }

    public List<TestQuestionDTO> getTestQuestions(
            Integer deckId,
            Integer numQuestions,
            Boolean trueFalse,
            Boolean multipleChoice,
            Boolean written,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        Integer userId = user.getId();

        Deck deck = deckRepository.findById(deckId).orElseThrow(() -> new ResourceNotFoundException("Deck not found"));

        if(deck.isPrivate() && !deck.getUserId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to view cards from this deck");
        }

        List<Card> allCards = cardRepository.findAllByDeckId(deckId);

        Collections.shuffle(allCards);

        List<Card> selectedCards = allCards.subList(0, numQuestions);

        List<TestQuestionDTO> testQuestions = new ArrayList<>();
        Random random = new Random();

        for (Card card : selectedCards) {
            int totalTypes = (trueFalse ? 1 : 0) + (multipleChoice ? 1 : 0) + (written ? 1 : 0);
            int rand = random.nextInt(totalTypes);

            TestQuestionDTO question = new TestQuestionDTO();
            if (rand < (trueFalse ? 1 : 0)) {
                boolean isTrueQuestion = random.nextBoolean();

                question.setQuestion(card.getFrontText());
                question.setChoiceTF(isTrueQuestion ? card.getBackText() : getRandomIncorrectBackText(allCards, card));
                question.setAnswerTF(isTrueQuestion);

            } else if (rand < (trueFalse ? 1 : 0) + (multipleChoice ? 1 : 0)) {
                // Create a list of unique backTexts excluding the correct answer
                List<String> uniqueBackTexts = allCards.stream()
                        .filter(card_ -> !card_.getBackText().equals(card.getBackText()))
                        .map(Card::getBackText)
                        .distinct()
                        .collect(Collectors.toList());
                // Shuffle the list and take three distinct random backTexts
                Collections.shuffle(uniqueBackTexts);
                List<String> choices = new ArrayList<>();
                choices.add(card.getBackText());
                choices.addAll(uniqueBackTexts.subList(0, Math.min(3, uniqueBackTexts.size())));

                // Shuffle the choices to randomize the order
                Collections.shuffle(choices);
                question.setQuestion(card.getFrontText());
                question.setChoices(choices.toArray(new String[0]));
                question.setAnswerMC(choices.indexOf(card.getBackText()));
            } else {
                question.setQuestion(card.getFrontText());
                question.setAnswerWritten(card.getBackText());
            }
            testQuestions.add(question);
        }
        return testQuestions;
    }

    private String getRandomIncorrectBackText(List<Card> cards, Card selectedCard) {
        Random random = new Random();
        String incorrectBackText;

        // Keep generating a random backText until it is different from the selected card's backText
        do {
            Card randomCard = cards.get(random.nextInt(cards.size()));
            incorrectBackText = randomCard.getBackText();
        } while (incorrectBackText.equals(selectedCard.getBackText()));

        return incorrectBackText;
    }
}
