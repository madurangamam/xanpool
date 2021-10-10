package com.xanpool.assessment.rest.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@EqualsAndHashCode
public class CacheReference {

  private String fromCurrency;
  private String toCurrency;
  private String accessKey;
  private String amount;
}
