package com.esgi.leitnersystem.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Card not found")
public class CardNotFoundException extends RuntimeException {

  public CardNotFoundException(String message) { super(message); }
}
