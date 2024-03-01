package com.esgi.leitnersystem.infrastructure.exception;

public class UserAlreadyExistsException extends Throwable {
  public UserAlreadyExistsException(String message) { super(message); }
}
