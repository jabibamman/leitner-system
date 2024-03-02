package com.esgi.leitnersystem.domain.revision;

import com.esgi.leitnersystem.domain.card.CardRevision;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CardRevisionRepositoryPort {
  CardRevision save(CardRevision cardRevision);

  List<CardRevision> findByRevisionDate(LocalDate date);
  Optional<CardRevision> findLatestRevisionByCardId(UUID cardId);
  CardRevision findByCardId(UUID revisionId);
}
