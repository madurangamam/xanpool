package com.xanpool.assessment.rest.client;

import org.springframework.http.ResponseEntity;

import com.xanpool.assessment.rest.model.ApiResponse;

public interface CurrencyRateRestClient {

  ResponseEntity<ApiResponse> getRates(String accessKey);

}
