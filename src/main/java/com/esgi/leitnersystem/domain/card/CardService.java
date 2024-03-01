package com.esgi.leitnersystem.domain.card;

import com.esgi.leitnersystem.domain.category.Category;
import com.esgi.leitnersystem.domain.category.CategoryService;
import com.esgi.leitnersystem.infrastructure.dto.CardUserData;
import com.esgi.leitnersystem.infrastructure.exception.CardNotFoundException;
import com.esgi.leitnersystem.infrastructure.repository.CardRepository;
import com.esgi.leitnersystem.infrastructure.repository.CardRevisionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static com.esgi.leitnersystem.domain.card.Card.*;

@Service
public class CardService {
  private final CardRepository cardRepository;
  private CardRevisionRepository cardRevisionRepository;
  private final CategoryService categoryService;


  @Autowired
  public CardService(CardRepository cardRepository, CardRevisionRepository cardRevisionRepository, CategoryService categoryService) {
    this.cardRepository = cardRepository;
    this.cardRevisionRepository = cardRevisionRepository;
    this.categoryService = categoryService; // Initialise CategoryService
  }
  public List<Card> fetchAllCards(Optional<List<String>> tags) {
    if (tags.isPresent() && !tags.get().isEmpty()) {
      return cardRepository.findByTagsIn(tags.get());
    } else {
      return cardRepository.findAll();
    }
  }

  public Card createCard(CardUserData cardUserData) {
    Card card = builder()
            .question(cardUserData.getQuestion())
            .answer(cardUserData.getAnswer())
            .tag(cardUserData.getTag())
            .category(Category.FIRST)
            .build();

    return cardRepository.save(card);
  }

  private static boolean isValidDateFormat(String date) {
    try {
      // Tentative de parsing de la chaîne en LocalDate
      LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
      return true; // La chaîne a un format de date valide
    } catch (Exception e) {
      return false; // La chaîne n'a pas un format de date valide
    }
  }

  public List<Card> getCardsForQuizz(String date) {

    List<Card> allCards = cardRepository.findAll();

    if(date == null || date.isEmpty() || date.isBlank()) return allCards;

    if(!isValidDateFormat(date)) return List.of();

    LocalDate formattedDate = LocalDate.now();

    if (LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE).isBefore(formattedDate)) {
      return List.of();
    }

    List<CardRevision> revisionsForDate = cardRevisionRepository.findByRevisionDate(date);
    List<UUID> cardIdsRevisedToday = revisionsForDate.stream()
            .map(CardRevision::getCardId)
            .collect(Collectors.toList());
    List<Card> cardsToBeReviewed = allCards.stream()
            .filter(card -> !cardIdsRevisedToday.contains(card.getId()) || shouldBeReviewed(card, date))
            .collect(Collectors.toList());

    return cardsToBeReviewed;
  }

  private boolean shouldBeReviewed(Card card, String dateString) {
    CardRevision lastRevision = cardRevisionRepository.findTopByCardIdOrderByRevisionDateDesc(card.getId());
    if (lastRevision == null) {
      return true;
    }

    LocalDate lastRevisionDate = LocalDate.parse(lastRevision.getRevisionDate(), DateTimeFormatter.ISO_LOCAL_DATE);
    LocalDate currentDate = LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE);

    if (lastRevisionDate.isBefore(currentDate)) {
      long daysSinceLastRevision = ChronoUnit.DAYS.between(lastRevisionDate, currentDate);
      int expectedDaysUntilNextRevision = getDaysUntilNextRevision(card.getCategory());
      return daysSinceLastRevision >= expectedDaysUntilNextRevision;
    }

    return false;
  }

  private int getDaysUntilNextRevision(Category category) {
    switch (category) {
      case FIRST:
        return 1;
      case SECOND:
        return 2;
      case THIRD:
        return 4;
      case FOURTH:
        return 8;
      case FIFTH:
        return 16;
      case SIXTH:
        return 32;
      case SEVENTH:
        return 64;
      default:
        return Integer.MAX_VALUE;
    }
  }

  @Transactional
  public void processCardAnswer(UUID cardId, boolean isValid) throws CardNotFoundException {
    Optional<Card> cardOptional = cardRepository.findById(cardId);
    if (!cardOptional.isPresent()) {
      throw new CardNotFoundException("Card with ID " + cardId + " not found");
    }

    Card card = cardOptional.get();
    if (isValid) {
      categoryService.promoteCard(card);
    } else {
      categoryService.demoteCardToFirst(card);
    }

    String formattedDate = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
    CardRevision revision = new CardRevision(card.getId(), formattedDate, isValid);
    cardRevisionRepository.save(revision);
  }

}

