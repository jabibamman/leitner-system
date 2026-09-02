package com.esgi.leitnersystem.infrastructure.repository.adapter;

import com.esgi.leitnersystem.domain.card.Card;
import com.esgi.leitnersystem.domain.card.CardRepositoryPort;
import com.esgi.leitnersystem.domain.card.CardRevision;
import com.esgi.leitnersystem.domain.revision.CardRevisionRepositoryPort;
import com.esgi.leitnersystem.infrastructure.repository.CardRevisionRepository;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardRevisionRepositoryAdapter
    implements CardRevisionRepositoryPort {
  private final CardRevisionRepository cardRevisionRepository;

  @Autowired
  public CardRevisionRepositoryAdapter(
      CardRevisionRepository cardRevisionRepository) {
    this.cardRevisionRepository = cardRevisionRepository;
  }

  @Override
  public CardRevision save(CardRevision cardRevision) {
    return cardRevisionRepository.save(cardRevision);
  }

  @Override
  public List<CardRevision> findByRevisionDate(LocalDate date) {
    return cardRevisionRepository.findByRevisionDate(String.valueOf(date));
  }

  @Override
  public Optional<CardRevision> findLatestRevisionByCardId(UUID cardId) {
    return cardRevisionRepository.findTopByCardIdOrderByRevisionDateDesc(
        cardId);
  }

  @Override
  public CardRevision findByCardId(UUID revisionId) {
    return cardRevisionRepository.findByCardId(revisionId);
  }

  @Override
  public Map<UUID, CardRevision> findLatestRevisionPerCard() {
    // Deux revisions peuvent partager la meme date (carte repondue deux fois
    // le meme jour) : la requete renvoie alors les deux lignes pour cette
    // carte. Laquelle des deux est gardee n'a pas d'importance ici, seule la
    // date compte pour l'eligibilite au quiz.
    return cardRevisionRepository.findLatestRevisionPerCard()
        .stream()
        .collect(Collectors.toMap(CardRevision::getCardId,
                                  Function.identity(), (a, b) -> a));
  }
}
