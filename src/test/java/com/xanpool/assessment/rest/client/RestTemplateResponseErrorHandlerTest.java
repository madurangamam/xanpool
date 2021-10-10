package com.xanpool.assessment.rest.client;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.mock.http.client.MockClientHttpResponse;

import com.xanpool.assessment.exception.RestTemplateResponseErrorHandler;

import lombok.SneakyThrows;

class RestTemplateResponseErrorHandlerTest {

  @Mock
  private RestTemplateResponseErrorHandler restTemplateResponseErrorHandler;
  private ClientHttpResponse clientHttpResponse;

  @BeforeEach
  void setUp() {
    initMocks(this);
    clientHttpResponse = new MockClientHttpResponse(new byte[2], HttpStatus.BAD_REQUEST);
  }

  @SneakyThrows
  @Test
  void testHasError() {
    assertFalse(restTemplateResponseErrorHandler.hasError(clientHttpResponse));
  }
}
