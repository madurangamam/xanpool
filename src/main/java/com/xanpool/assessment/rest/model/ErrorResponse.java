package com.xanpool.assessment.rest.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {

  private String error;
  private String code;

}
