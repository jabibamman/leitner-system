package com.esgi.leitnersystem.domain.revision;

import com.esgi.leitnersystem.domain.card.Card;
import com.esgi.leitnersystem.domain.card.CardRevision;
import com.esgi.leitnersystem.domain.category.Category;
import com.esgi.leitnersystem.domain.category.CategoryService;
import com.esgi.leitnersystem.infrastructure.repository.CardRevisionRepository;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RevisionService {
  private final CardRevisionRepositoryPort cardRevisionRepository;
  private final CategoryService categoryService;

  @Autowired
  public RevisionService(CardRevisionRepositoryPort cardRevisionRepository,
                         CategoryService categoryService) {
    this.cardRevisionRepository = cardRevisionRepository;
    this.categoryService = categoryService;
  }

  public boolean isEligibleForQuiz(Card card, LocalDate date) {
    if (card.getCategory() == Category.DONE) {
      return false;
    }

    var lastRevision =
        cardRevisionRepository.findLatestRevisionByCardId(card.getId());
    return lastRevision.map(revision -> shouldBeReviewed(card, revision, date))
        .orElse(true);
  }

  public CardRevision findLatestRevisionByCardId(UUID cardId) {
    return cardRevisionRepository.findLatestRevisionByCardId(cardId).orElse(
        null);
  }

  public boolean shouldBeReviewed(Card card, CardRevision lastRevision,
                                  LocalDate currentDate) {
    var lastRevisionDate = LocalDate.parse(lastRevision.getRevisionDate(),
                                           DateTimeFormatter.ISO_LOCAL_DATE);
    long daysSinceLastRevision =
        ChronoUnit.DAYS.between(lastRevisionDate, currentDate);
    int expectedDaysUntilNextRevision =
        categoryService.getDaysUntilNextRevision(card.getCategory());
    return daysSinceLastRevision >= expectedDaysUntilNextRevision;
  }

  @Transactional
  public void recordCardRevision(Card card, boolean isValid) {
    var formattedDate =
        LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
    var revision = new CardRevision(card.getId(), formattedDate, isValid);
    cardRevisionRepository.save(revision);
  }
}
