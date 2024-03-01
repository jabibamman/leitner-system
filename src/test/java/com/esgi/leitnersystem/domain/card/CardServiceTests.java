package com.esgi.leitnersystem.domain.card;

import com.esgi.leitnersystem.domain.card.Card;
import com.esgi.leitnersystem.domain.card.CardService;
import com.esgi.leitnersystem.domain.category.CategoryService;
import com.esgi.leitnersystem.infrastructure.dto.CardUserData;
import com.esgi.leitnersystem.infrastructure.exception.CardNotFoundException;
import com.esgi.leitnersystem.infrastructure.repository.CardRepository;
import com.esgi.leitnersystem.infrastructure.repository.CardRevisionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class CardServiceTests {
  @Mock
  private CardRepository cardRepository;

  @Mock
  private CardRevisionRepository cardRevisionRepository;

  @Mock
  private CategoryService categoryService;

  @InjectMocks
  private CardService cardService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void fetchAllCards_ShouldReturnAllCards() {
    Card card1 = new Card();
    Card card2 = new Card();
    when(cardRepository.findAll()).thenReturn(List.of(card1, card2));

    List<Card> cards = cardService.fetchAllCards(Optional.empty());

    assertEquals(2, cards.size());
    verify(cardRepository, times(1)).findAll();
  }

  @Test
  void fetchAllCards_WithTags_ShouldReturnFilteredCards() {
    List<String> tags = List.of("tag1", "tag2");
    Card card1 = new Card();
    Card card2 = new Card();
    when(cardRepository.findByTagsIn(tags)).thenReturn(List.of(card1, card2));

    List<Card> cards = cardService.fetchAllCards(Optional.of(tags));

    assertEquals(2, cards.size());
    verify(cardRepository, times(1)).findByTagsIn(tags);
  }

  @Test
  void createCard_ShouldReturnCreatedCard() {
    CardUserData cardUserData = new CardUserData();
    Card expectedCard = new Card();
    when(cardRepository.save(any())).thenReturn(expectedCard);

    Card createdCard = cardService.createCard(cardUserData);

    assertEquals(expectedCard, createdCard);
    verify(cardRepository, times(1)).save(any());
  }

  @Test
  void processCardAnswer_WhenCardNotFound_ShouldThrowCardNotFoundException() {
    UUID cardId = UUID.randomUUID();
    boolean isValid = true;
    when(cardRepository.findById(cardId)).thenReturn(Optional.empty());

    assertThrows(CardNotFoundException.class, () -> cardService.processCardAnswer(cardId, isValid));
  }

  @Test
  void processCardAnswer_WhenCardFoundAndValid_ShouldPromoteCard() throws CardNotFoundException {
    UUID cardId = UUID.randomUUID();
    boolean isValid = true;
    Card card = new Card();
    when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));

    cardService.processCardAnswer(cardId, isValid);

    verify(categoryService, times(1)).promoteCard(card);
    verify(cardRevisionRepository, times(1)).save(any());
  }

  @Test
  void processCardAnswer_WhenCardFoundAndInvalid_ShouldDemoteCard() throws CardNotFoundException {
    UUID cardId = UUID.randomUUID();
    boolean isValid = false;
    Card card = new Card();
    when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));

    cardService.processCardAnswer(cardId, isValid);

    verify(categoryService, times(1)).demoteCardToFirst(card);
    verify(cardRevisionRepository, times(1)).save(any());
  }

  @Test
  void getCardsForQuizz_WithFutureDate_ShouldReturnAllCards() {
    String date = "2025-03-21";
    List<Card> expectedCards = List.of(new Card(), new Card());
    when(cardRepository.findAll()).thenReturn(expectedCards);

    List<Card> actualCards = cardService.getCardsForQuizz(date);

    assertEquals(expectedCards, actualCards);
  }

  @Test
  void getCardsForQuizz_WithPastDate_ShouldReturnEmptyList() {
    String date = LocalDate.now().minusDays(1).toString();

    List<Card> actualCards = cardService.getCardsForQuizz(date);

    assertEquals(0, actualCards.size());
  }

  @Test
  void getCardsForQuizz_WithInvalidDate_ShouldReturnNoCards() {
    String date = "invalid-date";
    List<Card> expectedCards = List.of();

    List<Card> actualCards = cardService.getCardsForQuizz(date);

    assertEquals(expectedCards, actualCards);
  }
}