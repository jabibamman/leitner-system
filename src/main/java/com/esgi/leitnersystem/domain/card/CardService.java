package com.esgi.leitnersystem.domain.card;

import com.esgi.leitnersystem.infrastructure.dto.CardUserData;
import com.esgi.leitnersystem.domain.card.Card;
import com.esgi.leitnersystem.domain.category.Category;
import com.esgi.leitnersystem.infrastructure.repository.CardRepository;
import java.util.List;
import java.util.Optional;

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
