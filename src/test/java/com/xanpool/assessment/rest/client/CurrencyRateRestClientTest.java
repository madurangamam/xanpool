package com.xanpool.assessment.rest.client;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

public class CurrencyRateRestClientTest {

  @Test
  public void constructUriEncoded() {

    final UriComponents uriComponents = UriComponentsBuilder.newInstance()
        .scheme("http").host("data.fixer.io")
        .path("/api/latest").query("access_key={access_key}").buildAndExpand("649a1b0b0f0a325f910650da84f7a604");

    assertEquals("http://data.fixer.io/api/latest?access_key=649a1b0b0f0a325f910650da84f7a604", uriComponents.toUriString());
  }

}
