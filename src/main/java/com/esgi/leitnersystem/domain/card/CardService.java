package com.esgi.leitnersystem.domain.card;

import com.esgi.leitnersystem.domain.Quiz.QuizService;
import com.esgi.leitnersystem.domain.category.Category;
import com.esgi.leitnersystem.domain.category.CategoryService;
import com.esgi.leitnersystem.domain.revision.RevisionService;
import com.esgi.leitnersystem.infrastructure.dto.CardUserData;
import com.esgi.leitnersystem.infrastructure.exception.CardNotFoundException;
import com.esgi.leitnersystem.infrastructure.repository.CardRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CardService {
  private final CardRepositoryPort cardRepository;
  private final CategoryService categoryService;
  private final QuizService quizService;
  private final RevisionService revisionService;

  @Autowired
  public CardService(CardRepositoryPort cardRepository,
                     CategoryService categoryService, QuizService quizService,
                     RevisionService revisionService) {
    this.cardRepository = cardRepository;
    this.categoryService = categoryService;
    this.quizService = quizService;
    this.revisionService = revisionService;
  }

  public Card createCard(CardUserData cardUserData) {
    var type = cardUserData.getType() != null ? cardUserData.getType()
                                              : CardType.ATOMIC;
    var card = Card.builder()
                   .question(cardUserData.getQuestion())
                   .answer(cardUserData.getAnswer())
                   .tag(cardUserData.getTag())
                   .category(Category.FIRST)
                   .type(type)
                   .build();

    return cardRepository.save(card);
  }

  public List<Card> fetchAllCards(Optional<List<String>> tags) {
    return fetchAllCards(tags, Optional.empty());
  }

  public List<Card> fetchAllCards(Optional<List<String>> tags,
                                  Optional<CardType> type) {
    List<Card> cards =
        tags.map(t
                 -> t.stream().map(String::toLowerCase).collect(
                     Collectors.toList()))
            .map(cardRepository::findByTagsIn)
            .orElseGet(cardRepository::findAll);

    return filterByType(cards, type);
  }

  public List<Card> getCardsForQuizz(LocalDate date) {
    return getCardsForQuizz(date, Optional.empty());
  }

  public List<Card> getCardsForQuizz(LocalDate date, Optional<CardType> type) {
    return filterByType(quizService.getCardsDueForQuiz(date), type);
  }

  // Une carte sans type (donnee existante avant l'introduction du champ) est
  // traitee comme ATOMIC : c'etait le seul mode disponible jusque-la.
  private List<Card> filterByType(List<Card> cards, Optional<CardType> type) {
    return type.map(t
                     -> cards.stream()
                            .filter(card
                                    -> (card.getType() != null
                                            ? card.getType()
                                            : CardType.ATOMIC) == t)
                            .collect(Collectors.toList()))
        .orElse(cards);
  }

  @Transactional
  public void processCardAnswer(UUID cardId, boolean isValid)
      throws CardNotFoundException {
    var card = cardRepository.findById(cardId).orElseThrow(
        ()
            -> new CardNotFoundException("Card with ID " + cardId +
                                         " not found"));

    if (isValid) {
      categoryService.promoteCard(card);
      if (card.getCategory() == Category.DONE) {
        markCardAsDone(card);
      }
    } else {
      categoryService.demoteCardToFirst(card);
    }

    recordCardRevision(card, isValid);
  }

  private void markCardAsDone(Card card) {
    card.setCategory(Category.DONE);
    cardRepository.save(card);
  }

  private void recordCardRevision(Card card, boolean isValid) {
    revisionService.recordCardRevision(card, isValid);
  }

  public void updateCard(Card currentCard) { cardRepository.save(currentCard); }
}
