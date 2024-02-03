package com.esgi.leitnersystem.infrastructure.web.controller;

import com.esgi.leitnersystem.application.dto.CardUserData;
import com.esgi.leitnersystem.application.service.CardService;
import com.esgi.leitnersystem.domain.model.Card;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cards")
public class CardsController {
  private final CardService cardService;

  public CardsController(CardService cardService) {
    this.cardService = cardService;
  }

  @GetMapping
  public ResponseEntity<List<Card>> getAllCards() {
    return ResponseEntity.ok().body(cardService.fetchAllCards());
  }

  @PostMapping
  public ResponseEntity<Card>
  createCard(@RequestBody CardUserData cardUserData) {
    /* TODO: return a card */
    return ResponseEntity.status(201).body(new Card());
  }

  @GetMapping("/quizz")
  public ResponseEntity<List<Card>>
  getCardsForQuizz(@RequestParam(required = false) String date) {
    // TODO: return cards for a quizz
    return ResponseEntity.ok().body(List.of());
  }

  @PatchMapping("/{cardId}/answer")
  public ResponseEntity<Void>
  answerCard(@PathVariable String cardId,
             @RequestBody Map<String, Boolean> answerBody) {
    // TODO: handle the answer to a card
    return ResponseEntity.noContent().build();
  }
}
