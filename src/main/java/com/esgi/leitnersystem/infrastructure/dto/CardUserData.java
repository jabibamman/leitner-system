package com.esgi.leitnersystem.infrastructure.dto;

import com.esgi.leitnersystem.domain.card.CardType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Schema(name = "CardUserData", description = "Data to create a new card")
@AllArgsConstructor
@RequiredArgsConstructor
public class CardUserData {

  @NotBlank(message = "Question is mandatory")
  @Size(max = 4000, message = "Question must be at most 4000 characters")
  @Schema(description = "Question to be asked to the user during a quizz",
          example = "What is pair programming?", required = true)
  private String question;

  @NotBlank(message = "Answer is mandatory")
  @Size(max = 4000, message = "Answer must be at most 4000 characters")
  @Schema(description = "Expected answer for the question",
          example = "A practice to work in pair on the same computer.",
          required = true)
  private String answer;

  @Schema(description = "A tag to group cards on the same topic",
          example = "Teamwork")
  private String tag;

  @Schema(description =
              "Nature de la carte. ATOMIC (par defaut) pour une revision "
              + "silencieuse, ORAL pour une carte a repondre a voix haute.",
          example = "ATOMIC", defaultValue = "ATOMIC")
  private CardType type;

  // Conserve le constructeur a 3 arguments d'avant l'ajout du type, pour ne
  // pas casser les appelants existants : CardService applique deja ATOMIC
  // quand le type est absent.
  public CardUserData(String question, String answer, String tag) {
    this(question, answer, tag, null);
  }
}
