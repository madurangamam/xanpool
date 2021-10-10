package com.xanpool.assessment.rest.client.impl;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.xanpool.assessment.exception.InvalidAccessKeyException;
import com.xanpool.assessment.rest.client.CurrencyRateRestClient;
import com.xanpool.assessment.rest.model.ApiResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Component
@Slf4j
public class CurrencyRateRestClientImpl implements CurrencyRateRestClient {

  private final RestTemplate restTemplate;
  private final RetryTemplate retryTemplate;

  @Override
  public ResponseEntity<ApiResponse> getRates(String accessKey) {

    final UriComponents uriComponents = UriComponentsBuilder.newInstance()
        .scheme("http").host("data.fixer.io")
        .path("/api/latest").query("access_key={access_key}").buildAndExpand(accessKey);

    log.info("Request send to : {}", uriComponents.toUri());

    ResponseEntity<ApiResponse> apiResponse =
        retryTemplate.execute(retryContext ->
            restTemplate.exchange(
                uriComponents.toUri(),
                HttpMethod.GET,
                new HttpEntity<>(null),
                ApiResponse.class
            )
        );

    if(!apiResponse.getStatusCode().is2xxSuccessful()){
      throw new InvalidAccessKeyException("You have not supplied a valid API Access Key.");
    }

    return apiResponse;
  }
}
