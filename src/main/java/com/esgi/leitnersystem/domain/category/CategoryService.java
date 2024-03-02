package com.esgi.leitnersystem.domain.category;

import com.esgi.leitnersystem.domain.card.Card;
import com.esgi.leitnersystem.domain.card.CardRepositoryPort;
import com.esgi.leitnersystem.infrastructure.repository.CardRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

  private final CardRepositoryPort cardRepository;

  @Autowired
  public CategoryService(CardRepositoryPort cardRepository) {
    this.cardRepository = cardRepository;
  }

  public Card promoteCard(Card card) {
    Category currentCategory = card.getCategory();
    Category nextCategory = determineNextCategory(currentCategory);
    card.setCategory(nextCategory);
    return cardRepository.save(card);
  }

  private Category determineNextCategory(Category currentCategory) {
    return switch (currentCategory) {
            case FIRST -> Category.SECOND;
            case SECOND -> Category.THIRD;
            case THIRD -> Category.FOURTH;
            case FOURTH -> Category.FIFTH;
            case FIFTH -> Category.SIXTH;
            case SIXTH -> Category.SEVENTH;
            case SEVENTH, DONE -> Category.DONE;
            default -> throw new IllegalArgumentException("Unknown category: " + currentCategory);
        };
    }

    public Card demoteCardToFirst(Card card) {
        card.setCategory(Category.FIRST);
        return cardRepository.save(card);
    }

    public int getDaysUntilNextRevision(Category category) {
        return switch (category) {
            case FIRST -> 1;
            case SECOND -> 2;
            case THIRD -> 4;
            case FOURTH -> 8;
            case FIFTH -> 16;
            case SIXTH -> 32;
            case SEVENTH, DONE -> 64;
        };
    }
}
