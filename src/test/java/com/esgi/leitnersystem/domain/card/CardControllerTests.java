package com.esgi.leitnersystem.domain.card;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.esgi.leitnersystem.infrastructure.dto.CardUserData;
import com.esgi.leitnersystem.presentation.controller.CardsController;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CardControllerTests {
  @Mock private CardService cardService;

  @InjectMocks private CardsController cardsController;

  @Test
  public void getAllCards_ShouldReturnAllCards() {
    List<Card> expectedCards = Collections.singletonList(new Card());
    when(cardService.fetchAllCards(Optional.empty())).thenReturn(expectedCards);

    List<Card> actualCards = cardsController.getAllCards(null).getBody();

    assertEquals(expectedCards, actualCards);
  }

  @Test
  public void createCard_ShouldReturnCreatedCard() {
    CardUserData cardUserData = new CardUserData();
    Card createdCard = new Card();
    when(cardService.createCard(cardUserData)).thenReturn(createdCard);

    Object response = cardsController.createCard(cardUserData).getBody();

    assertEquals(createdCard, response);
  }

  @Test
  public void getCardsForQuizz_WithValidDate_ShouldReturnCards() {
    String date = "2024-02-21";
    List<Card> expectedCards = Collections.singletonList(new Card());
    when(cardService.getCardsForQuizz(LocalDate.parse(date)))
        .thenReturn(expectedCards);

    List<Card> actualCards = cardsController.getCardsForQuizz(date).getBody();

    assertEquals(expectedCards, actualCards);
  }

  @Test
  public void getCardsForQuizz_WithNullDate_ShouldReturnCards() {
    List<Card> expectedCards = Collections.singletonList(new Card());
    when(cardService.getCardsForQuizz(null)).thenReturn(expectedCards);

    List<Card> actualCards = cardsController.getCardsForQuizz(null).getBody();

    assertEquals(expectedCards, actualCards);
  }

  @Test
  public void getCardsForQuizz_WithEmptyDate_ShouldReturnCards() {
    List<Card> expectedCards = Collections.singletonList(new Card());
    when(cardService.getCardsForQuizz(LocalDate.now()))
        .thenReturn(expectedCards);

    List<Card> actualCards = cardsController.getCardsForQuizz("").getBody();

    assertEquals(expectedCards, actualCards);
  }

  @Test
  public void getCardsForQuizz_WithInvalidDate_ShouldReturnNoCards() {
    String invalidDate = "invalid-date";
    List<Card> expectedCards = Collections.emptyList();
    when(cardService.getCardsForQuizz(LocalDate.now()))
        .thenReturn(expectedCards);

    List<Card> actualCards =
        cardsController.getCardsForQuizz(invalidDate).getBody();

    assertEquals(expectedCards, actualCards);
  }
}
