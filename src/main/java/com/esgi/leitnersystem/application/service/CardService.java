package com.esgi.leitnersystem.application.service;

import com.esgi.leitnersystem.domain.model.Card;
import com.esgi.leitnersystem.domain.repository.CardRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CardService {
    private final CardRepository cardRepository;

    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public List<Card> fetchAllCards() {
        return cardRepository.findAll();
    }

}