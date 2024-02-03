package com.esgi.leitnersystem.infrastructure.config.persistence.adapter;

import com.esgi.leitnersystem.domain.model.Card;
import com.esgi.leitnersystem.domain.repository.CardRepository;
import java.util.List;

public class CardRepositoryImpl implements CardRepository {
  @Override
  public List<Card> findAll() {
    return List.of();
  }

  @Override
  public Card save(Card card) {
    return new Card();
  }

  @Override
  public List<Card> findCardsForQuizz(String date) {
    return List.of();
  }

  @Override
  public void answerCard(String cardId, boolean answer) {}
}