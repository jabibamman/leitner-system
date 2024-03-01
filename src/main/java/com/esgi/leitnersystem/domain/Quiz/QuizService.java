package com.esgi.leitnersystem.domain.Quiz;

import com.esgi.leitnersystem.domain.card.Card;
import com.esgi.leitnersystem.domain.card.CardRepositoryPort;
import com.esgi.leitnersystem.domain.revision.RevisionService;
import com.esgi.leitnersystem.infrastructure.repository.CardRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuizService {
  private final CardRepositoryPort cardRepository;
  private final RevisionService revisionService;

  @Autowired
  public QuizService(CardRepositoryPort cardRepository,
                     RevisionService revisionService) {
    this.cardRepository = cardRepository;
    this.revisionService = revisionService;
  }

  public List<Card> getCardsDueForQuiz(LocalDate date) {
    List<Card> allCards = cardRepository.findAll();
    return allCards.stream()
        .filter(card -> revisionService.isEligibleForQuiz(card, date))
        .collect(Collectors.toList());
  }
}
