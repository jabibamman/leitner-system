package com.esgi.leitnersystem.infrastructure.web.controller;

import org.springframework.web.bind.annotation.*;
import com.esgi.leitnersystem.domain.model.Card;
import com.esgi.leitnersystem.application.dto.CardUserData;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cards")
public class CardsController {

    @GetMapping
    public ResponseEntity<List<Card>> getAllCards(@RequestParam(required = false) List<String> tags) {
        //* TODO: list of cards */
        return ResponseEntity.ok().body(List.of());
    }

    @PostMapping
    public ResponseEntity<Card> createCard(@RequestBody CardUserData cardUserData) {
        /* TODO: return a card */
        return ResponseEntity.status(201).body(new Card());
    }

    @GetMapping("/quizz")
    public ResponseEntity<List<Card>> getCardsForQuizz(@RequestParam(required = false) String date) {
        // TODO: return cards for a quizz
        return ResponseEntity.ok().body(List.of());
    }

    @PatchMapping("/{cardId}/answer")
    public ResponseEntity<Void> answerCard(@PathVariable String cardId, @RequestBody Map<String, Boolean> answerBody) {
        // TODO: handle the answer to a card
        return ResponseEntity.noContent().build();
    }
}
