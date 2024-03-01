package com.esgi.leitnersystem.domain.Quiz;

import com.esgi.leitnersystem.domain.card.Card;
import com.esgi.leitnersystem.domain.revision.RevisionService;
import com.esgi.leitnersystem.infrastructure.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuizService {
    private final CardRepository cardRepository;
    private final RevisionService revisionService;

    @Autowired
    public QuizService(CardRepository cardRepository, RevisionService revisionService) {
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