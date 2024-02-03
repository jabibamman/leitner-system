package com.esgi.leitnersystem.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CardUserData {

  @NotBlank(message = "Question is mandatory")
  @Schema(description = "Question to be asked to the user during a quizz",
          example = "What is pair programming?", required = true)
  private String question;

  @NotBlank(message = "Answer is mandatory")
  @Schema(description = "Expected answer for the question",
          example = "A practice to work in pair on the same computer.",
          required = true)
  private String answer;

  @Schema(description = "A tag to group cards on the same topic",
          example = "Teamwork")
  private String tag;
}
