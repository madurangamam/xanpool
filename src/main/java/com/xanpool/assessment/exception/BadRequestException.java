package com.xanpool.assessment.exception;

public class BadRequestException extends RuntimeException{

  public BadRequestException(String message) {
    super(message);
  }
}
