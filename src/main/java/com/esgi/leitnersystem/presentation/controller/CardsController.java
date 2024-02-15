package com.esgi.leitnersystem.presentation.controller;

import com.esgi.leitnersystem.domain.card.Card;
import com.esgi.leitnersystem.domain.card.CardService;
import com.esgi.leitnersystem.infrastructure.dto.CardUserData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.http.HttpStatus;
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
  @Tag(name = "Cards")
  @Operation(
      summary = "Get all cards",
      description =
          "Used to fetch every card with given tags. If no tags are provided, will fetch all cards.",
      parameters =
      {
        @Parameter(
            name = "tags",
            description =
                "Tags of cards to find. If not present, all cards will be found.",
            example = "tag1,tag2",
            schema = @Schema(type = "array", implementation = String.class))
      },
      responses =
      {
        @ApiResponse(responseCode = "200", description = "Successful operation",
                     content = @Content(mediaType = "application/json"))
      })
  public ResponseEntity<List<Card>>
  getAllCards(@RequestParam(required = false) List<String> tags) {
    List<Card> cards = cardService.fetchAllCards(Optional.ofNullable(tags));
    return ResponseEntity.ok().body(cards);
  }

  @PostMapping
  @Tag(name = "Cards")
  @Operation(
      summary = "Create a new card",
      description =
          "Used to create a new card in the system. A new card will be present in the next quizz.")
  @ApiResponse(responseCode = "201", description = "Created card",
               content = @Content(mediaType = "application/json",
                                  schema = @Schema(implementation = Card.class))
               )
  @ApiResponse(responseCode = "400", description = "Bad request",
               content = @Content())
  public ResponseEntity<?>
  createCard(@RequestBody @Valid CardUserData cardUserData) {
    try {
      Card createdCard = cardService.createCard(cardUserData);
      return new ResponseEntity<>(createdCard, HttpStatus.CREATED);
    } catch (Exception e) {
      Logger.getLogger(CardsController.class.getName())
          .log(Level.SEVERE, "Error creating card", e);
      return ResponseEntity.badRequest().body("Error creating card: " +
                                              e.getMessage());
    }
  }

  @GetMapping("/quizz")
  @Tag(name = "Learning")
  public ResponseEntity<List<Card>>
  getCardsForQuizz(@RequestParam(required = false) String date) {
    // TODO: return cards for a quizz
    return ResponseEntity.ok().body(List.of());
  }

  @PatchMapping("/{cardId}/answer")
  @Tag(name = "Learning")
  public ResponseEntity<Void>
  answerCard(@PathVariable String cardId,
             @RequestBody Map<String, Boolean> answerBody) {
    // TODO: handle the answer to a card
    return ResponseEntity.noContent().build();
  }
}
