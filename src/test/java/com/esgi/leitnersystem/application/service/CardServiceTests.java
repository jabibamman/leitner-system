package com.esgi.leitnersystem.application.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.esgi.leitnersystem.domain.card.Card;
import com.esgi.leitnersystem.domain.card.CardService;
import com.esgi.leitnersystem.infrastructure.repository.CardRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CardServiceTests {
  @Mock private CardRepository cardRepository;

  private CardService cardService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    cardService = new CardService(cardRepository);
  }

  @Test
  void fetchAllCards_ShouldReturnAllCards() {
    Card card1 = new Card();
    Card card2 = new Card();
    when(cardRepository.findAll()).thenReturn(Arrays.asList(card1, card2));

    List<Card> cards = cardService.fetchAllCards(Optional.empty());

    assertThat(cards).hasSize(2);
    verify(cardRepository, times(1)).findAll();
  }
}
