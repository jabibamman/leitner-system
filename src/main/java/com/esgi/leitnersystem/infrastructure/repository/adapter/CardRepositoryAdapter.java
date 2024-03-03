package com.esgi.leitnersystem.infrastructure.repository.adapter;

import com.esgi.leitnersystem.domain.card.Card;
import com.esgi.leitnersystem.domain.card.CardRepositoryPort;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.esgi.leitnersystem.infrastructure.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardRepositoryAdapter implements CardRepositoryPort {
  private final CardRepository cardRepository;

  @Autowired
  public CardRepositoryAdapter(CardRepository cardRepository) {
    this.cardRepository = cardRepository;
  }

  @Override
  public Card save(Card card) {
    return cardRepository.save(card);
  }

  @Override
  public Optional<Card> findById(UUID cardId) {
    return cardRepository.findById(cardId);
  }

  @Override
  public List<Card> findAll() {
    return cardRepository.findAll();
  }

  @Override
  public List<Card> findByTagsIn(List<String> tags) {
    return cardRepository.findByTagsIn(tags);
  }

  @Override
  public Collection<Card> findAllByIdNotIn(List<UUID> cardIdsRevisedToday) {
    return cardRepository.findAllByIdNotIn(cardIdsRevisedToday);
  }
}
