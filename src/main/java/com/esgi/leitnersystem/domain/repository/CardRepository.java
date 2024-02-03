package com.esgi.leitnersystem.domain.repository;

import com.esgi.leitnersystem.domain.model.Card;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository {

  List<Card> findAll();

  Card save(Card card);

  List<Card> findCardsForQuizz(String date);

  void answerCard(String cardId, boolean answer);
}
