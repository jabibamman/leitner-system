package com.esgi.leitnersystem.presentation.controller;

import com.esgi.leitnersystem.domain.card.Card;
import com.esgi.leitnersystem.domain.card.CardService;
import com.esgi.leitnersystem.domain.card.CardType;
import com.esgi.leitnersystem.infrastructure.dto.AnswerDTO;
import com.esgi.leitnersystem.infrastructure.dto.CardUserData;
import com.esgi.leitnersystem.infrastructure.exception.CardNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
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
          "Used to fetch every cards with given tags. If no tags are provided, will fetch all cards.",
      parameters =
      {
        @Parameter(
            name = "tags",
            description =
                "Tags of cards to find. If not present, all cards will be found.",
            example = "tag1,tag2",
            schema = @Schema(type = "array", implementation = String.class)),
        @Parameter(
            name = "type",
            description =
                "Nature des cartes a filtrer (ATOMIC ou ORAL). Si absent, "
                + "les deux types sont renvoyes.",
            example = "ATOMIC",
            schema = @Schema(implementation = CardType.class))
      },
      responses =
      {
        @ApiResponse(
            responseCode = "200", description = "Successful operation",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(type = "array", implementation = Card.class))),
        @ApiResponse(responseCode = "400",
                     description = "Invalid type", content = @Content())
      })
  public ResponseEntity<?>
  getAllCards(@RequestParam(required = false) List<String> tags,
              @RequestParam(required = false) String type) {
    Optional<CardType> parsedType;
    try {
      parsedType = parseCardType(type);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }

    List<Card> cards =
        cardService.fetchAllCards(Optional.ofNullable(tags), parsedType);
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
  @Operation(
      summary = "Cards for the day",
      description =
          "sed to fetch all cards for a quizz at a given date. If no date is provided, quizz will be for today.",
      parameters =
      {
        @Parameter(
            name = "date",
            description =
                "Date of the quiz in the format 'YYYY-MM-DD'. If not provided, current date will be used.",
            example = "2024-02-21",
            schema = @Schema(type = "string", format = "date")),
        @Parameter(
            name = "type",
            description =
                "Nature de la session (ATOMIC ou ORAL). Determinant : "
                + "melanger les deux types dans une meme session fait "
                + "disparaitre l'oral au profit du silencieux. Si absent, "
                + "les deux types sont renvoyes.",
            example = "ATOMIC",
            schema = @Schema(implementation = CardType.class))
      },
      responses =
      {
        @ApiResponse(
            responseCode = "200", description = "Successful operation",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(type = "array", implementation = Card.class))),
        @ApiResponse(responseCode = "400",
                     description = "Invalid date or type", content = @Content())
      })
  public ResponseEntity<?>
  getCardsForQuizz(@RequestParam(required = false) String date,
                   @RequestParam(required = false) String type) {
    LocalDate parsedDate;
    try {
      parsedDate = (date != null && !date.isEmpty())
          ? LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE)
          : LocalDate.now();
    } catch (java.time.format.DateTimeParseException e) {
      return ResponseEntity.badRequest().body(
          "Invalid date, expected format YYYY-MM-DD: " + date);
    }

    Optional<CardType> parsedType;
    try {
      parsedType = parseCardType(type);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }

    List<Card> cards = cardService.getCardsForQuizz(parsedDate, parsedType);
    return ResponseEntity.ok().body(cards);
  }

  private Optional<CardType> parseCardType(String type) {
    if (type == null || type.isBlank()) {
      return Optional.empty();
    }
    try {
      return Optional.of(CardType.valueOf(type.trim().toUpperCase()));
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(
          "Invalid type, expected ATOMIC or ORAL: " + type);
    }
  }

  @PatchMapping("/{cardId}/answer")
  @Tag(name = "Learning")
  @Operation(
      summary = "Answer a question",
      description =
          "Used to answer a question. Body indicate if user has answered correctly or not.",
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "User's answer for the card.",
          content = @Content(mediaType = "application/json",
                             schema = @Schema(implementation = AnswerDTO.class))
          ),
      responses =
      {
        @ApiResponse(responseCode = "204",
                     description = "Answer submitted successfully")
        ,
            @ApiResponse(responseCode = "404", description = "Card not found"),
            @ApiResponse(responseCode = "400", description = "Bad request")
      })
  public ResponseEntity<Void>
  answerCard(@PathVariable UUID cardId,
             @RequestBody @Valid AnswerDTO answerDTO) {
    try {
      cardService.processCardAnswer(cardId, answerDTO.getIsValid());
      return ResponseEntity.noContent().build();
    } catch (CardNotFoundException e) {
      return ResponseEntity.notFound().build();
    } catch (Exception e) {
      Logger.getLogger(CardService.class.getName())
          .log(Level.SEVERE, "Error processing card answer", e);
      return ResponseEntity.badRequest().build();
    }
  }
}
