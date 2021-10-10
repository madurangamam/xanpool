package com.xanpool.assessment.rest.controller;

import java.util.concurrent.ExecutionException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.util.concurrent.UncheckedExecutionException;
import com.xanpool.assessment.cache.ExchangeRateCache;
import com.xanpool.assessment.exception.InvalidAccessKeyException;
import com.xanpool.assessment.exception.InvalidCurrencyException;
import com.xanpool.assessment.exception.InvalidPriceException;
import com.xanpool.assessment.rest.model.CacheReference;
import com.xanpool.assessment.rest.model.ErrorResponse;
import com.xanpool.assessment.rest.model.PairRateResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CurrencyConvertController {

  private final ExchangeRateCache exchangeRateCache;

  @GetMapping(value = "/convert", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> posts(@RequestParam String access_key,
      @RequestParam String from,
      @RequestParam String to,
      @RequestParam(required = false) String amount) {

    try {
      PairRateResponse pairRateResponse = exchangeRateCache.getCache(
          getCacheReference(from, to, access_key, amount));
      return ResponseEntity.ok().body(pairRateResponse);
    } catch (InvalidAccessKeyException e) {
      return buildResponse(HttpStatus.SWITCHING_PROTOCOLS, e.getMessage(), "101");
    } catch (InvalidCurrencyException | InvalidPriceException | UncheckedExecutionException e) {
      return buildResponse(HttpStatus.BAD_REQUEST, e.getMessage(), "400");
    } catch (ExecutionException e) {
      return buildResponse(HttpStatus.CONFLICT, e.getMessage(), "409");
    } catch (Exception e) {
      log.error("Exception occurs at runtime :", e);
      return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "500");
    }

  }

  private ResponseEntity<Object> buildResponse(
      final HttpStatus httpStatus, final String message, final String code) {
    return ResponseEntity.status(httpStatus)
        .body(ErrorResponse.builder().error(message).code(code).build());
  }

  private CacheReference getCacheReference(String from, String to, String accessesKey,
      String amount) {
    CacheReference cacheReference = new CacheReference();
    cacheReference.setFromCurrency(from);
    cacheReference.setToCurrency(to);
    cacheReference.setAccessKey(accessesKey);

    if (amount == null) {
      cacheReference.setAmount("1");
    } else {
      cacheReference.setAmount(amount);
    }
    return cacheReference;
  }

}
