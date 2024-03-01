package com.esgi.leitnersystem.domain.card;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CardRepositoryPort {
    Card save(Card card);
    Optional<Card> findById(UUID cardId);
    List<Card> findAll();
    List<Card> findByTagsIn(List<String> tags);
    Collection<Card> findAllByIdNotIn(List<UUID> cardIdsRevisedToday);
}
