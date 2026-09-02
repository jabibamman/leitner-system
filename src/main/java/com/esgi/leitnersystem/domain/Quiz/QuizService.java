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
    // La derniere revision de chaque carte est recuperee en une seule
    // requete plutot qu'une par carte : sans ca, cet appel declenche
    // 1 + N requetes SQL (N = nombre de cartes).
    var latestRevisions = revisionService.findLatestRevisions();
    return allCards.stream()
        .filter(card -> revisionService.isEligibleForQuiz(card, date, latestRevisions))
        .collect(Collectors.toList());
  }
}
