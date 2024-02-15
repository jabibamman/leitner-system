package com.esgi.leitnersystem.domain.category;
/*
    Category:
      type: string
      description: Category of card indicating how many times you answered it
   and appearance frequency example: FIRST
 */

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(
    description =
        "Category of card indicating how many times you answered it and appearance frequency")
@Getter
@AllArgsConstructor
public enum Category {
  @Schema(example = "FIRST") FIRST,
  SECOND,
  THIRD,
  FOURTH,
  FIFTH,
  SIXTH,
  SEVENTH,
  DONE;
}
