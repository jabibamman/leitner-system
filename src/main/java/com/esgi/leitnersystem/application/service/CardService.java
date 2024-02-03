package com.esgi.leitnersystem.application.service;

import com.esgi.leitnersystem.domain.model.Card;
import com.esgi.leitnersystem.domain.repository.CardRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CardService {
  private final CardRepository cardRepository;

  public CardService(CardRepository cardRepository) {
    this.cardRepository = cardRepository;
  }

  public List<Card> fetchAllCards() { return cardRepository.findAll(); }
}
