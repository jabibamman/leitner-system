package com.esgi.leitnersystem.domain.card;

<<<<<<< Updated upstream:src/main/java/com/esgi/leitnersystem/domain/card/CardService.java
import com.esgi.leitnersystem.domain.card.Card;
import com.esgi.leitnersystem.domain.category.Category;
import com.esgi.leitnersystem.infrastructure.dto.CardUserData;
import com.esgi.leitnersystem.infrastructure.repository.CardRepository;
import java.util.List;
import java.util.Optional;
=======
import com.esgi.leitnersystem.application.dto.CardUserData;
import com.esgi.leitnersystem.domain.model.Card;
import com.esgi.leitnersystem.domain.model.Category;
import com.esgi.leitnersystem.infrastructure.repository.CardRepository;
import java.util.List;
import java.util.Optional;

>>>>>>> Stashed changes:src/main/java/com/esgi/leitnersystem/application/service/CardService.java
import org.springframework.stereotype.Service;

@Service
public class CardService {
  private final CardRepository cardRepository;

  public CardService(CardRepository cardRepository) {
    this.cardRepository = cardRepository;
  }

  public List<Card> fetchAllCards(Optional<List<String>> tags) {
    if (tags.isPresent() && !tags.get().isEmpty()) {
      return cardRepository.findByTagsIn(tags.get());
    } else {
      return cardRepository.findAll();
    }
  }
  public Card createCard(CardUserData cardUserData) {
    Card card = Card.builder()
                    .question(cardUserData.getQuestion())
                    .answer(cardUserData.getAnswer())
                    .tag(cardUserData.getTag())
                    .category(Category.FIRST)
                    .build();

    return cardRepository.save(card);
  }
}
