package com.esgi.leitnersystem.infrastructure.exception;

public class UnauthorizedException extends RuntimeException {
  public UnauthorizedException(String message) { super(message); }
}
