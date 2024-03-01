package com.esgi.leitnersystem.infrastructure.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(CardNotFoundException.class)
  protected ResponseEntity<Object>
  handleCardNotFound(CardNotFoundException ex) {
    ApiError apiError = new ApiError(
        HttpStatus.NOT_FOUND, ex.getLocalizedMessage(), "Card not found");
    return buildResponseEntity(apiError);
  }

  @ExceptionHandler(UnauthorizedException.class)
  public ResponseEntity<Object>
  handleUnauthorizedException(UnauthorizedException ex) {
    ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED,
                                     ex.getLocalizedMessage(), "Unauthorized");
    return buildResponseEntity(apiError);
  }

  @ExceptionHandler(UserAlreadyExistsException.class)
  public ResponseEntity<Object>
  handleUserAlreadyExists(UserAlreadyExistsException ex) {
    ApiError apiError = new ApiError(
        HttpStatus.CONFLICT, ex.getLocalizedMessage(), "User already exists");
    return buildResponseEntity(apiError);
  }

  private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
    return new ResponseEntity<>(apiError, apiError.getStatus());
  }

  @Getter
  @Setter
  @AllArgsConstructor
  public static class ApiError {
    private HttpStatus status;
    private String message;
    private String error;
  }
}
