package com.esgi.leitnersystem.infrastructure.dto;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AnswerDTO {
  private boolean isValid;

  public boolean getIsValid() { return isValid; }

  public void setIsValid(boolean isValid) { this.isValid = isValid; }
}
