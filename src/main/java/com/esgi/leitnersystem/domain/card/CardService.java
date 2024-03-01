package com.esgi.leitnersystem.domain.card;

import com.esgi.leitnersystem.domain.category.Category;
import com.esgi.leitnersystem.domain.category.CategoryService;
import com.esgi.leitnersystem.infrastructure.dto.CardUserData;
import com.esgi.leitnersystem.infrastructure.exception.CardNotFoundException;
import com.esgi.leitnersystem.infrastructure.repository.CardRepository;
import com.esgi.leitnersystem.infrastructure.repository.CardRevisionRepository;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CardService {
  private final CardRepository cardRepository;
  private final CardRevisionRepository cardRevisionRepository;
  private final CategoryService categoryService;

  @Autowired
  public CardService(CardRepository cardRepository,
                     CardRevisionRepository cardRevisionRepository,
                     CategoryService categoryService) {
    this.cardRepository = cardRepository;
    this.cardRevisionRepository = cardRevisionRepository;
    this.categoryService = categoryService;
  }

  public List<Card> fetchAllCards(Optional<List<String>> tags) {
    return tags.map(cardRepository::findByTagsIn).orElseGet(cardRepository::findAll);
  }

  public Card createCard(CardUserData cardUserData) {
    var card = Card.builder()
            .question(cardUserData.getQuestion())
            .answer(cardUserData.getAnswer())
            .tag(cardUserData.getTag())
            .category(Category.FIRST)
            .build();

    return cardRepository.save(card);
  }

  public List<Card> getCardsForQuizz(LocalDate date) {
    var allCards = cardRepository.findAll();
    return allCards.stream()
            .filter(card -> isEligibleForQuiz(card, date))
            .collect(Collectors.toList());
  }

  private boolean isEligibleForQuiz(Card card, LocalDate date) {
    if (card.getCategory() == Category.DONE) {
      return false;
    }

    var lastRevision = cardRevisionRepository.findTopByCardIdOrderByRevisionDateDesc(card.getId());
    return lastRevision.map(revision -> shouldBeReviewed(card, revision, date))
            .orElse(true);
  }

  private boolean shouldBeReviewed(Card card, CardRevision lastRevision, LocalDate currentDate) {
    var lastRevisionDate = LocalDate.parse(lastRevision.getRevisionDate(), DateTimeFormatter.ISO_LOCAL_DATE);
    long daysSinceLastRevision = ChronoUnit.DAYS.between(lastRevisionDate, currentDate);
    int expectedDaysUntilNextRevision = categoryService.getDaysUntilNextRevision(card.getCategory());
    return daysSinceLastRevision >= expectedDaysUntilNextRevision;
  }

  @Transactional
  public void processCardAnswer(UUID cardId, boolean isValid) throws CardNotFoundException {
    var card = cardRepository.findById(cardId)
            .orElseThrow(() -> new CardNotFoundException("Card with ID " + cardId + " not found"));

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
    var formattedDate = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
    var revision = new CardRevision(card.getId(), formattedDate, isValid);
    cardRevisionRepository.save(revision);
  }
}
