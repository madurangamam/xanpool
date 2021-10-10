package com.xanpool.assessment.facade.impl;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.UncheckedExecutionException;
import com.xanpool.assessment.cache.OracleCache;
import com.xanpool.assessment.exception.InvalidCurrencyException;
import com.xanpool.assessment.exception.InvalidPriceException;
import com.xanpool.assessment.facade.CurrencyRateCalculateFacade;
import com.xanpool.assessment.rest.model.ApiResponse;
import com.xanpool.assessment.rest.model.PairRateResponse;
import com.xanpool.assessment.rest.model.Query;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Component
@Slf4j
public class CurrencyRateCalculateFacadeImpl implements CurrencyRateCalculateFacade {

  private final OracleCache oracleCache;

  @Override
  public PairRateResponse calculateCurrencyRate(String accessKey, String from, String to,
      String amount) throws ExecutionException {

    ApiResponse apiResponse = oracleCache.getCache(accessKey);
    double amountToCalculate = 1.0;
    if (amount != null) {
      try {
        amountToCalculate = Double.parseDouble(amount);
      } catch (NumberFormatException e) {
        log.error("Invalid price amount",e);
        throw new InvalidPriceException("Invalid price amount");
      }
    }

    ObjectMapper mapper = new ObjectMapper();
    JSONObject jsonObject = null;
    try {
      String jsonString = mapper.writeValueAsString(Objects.requireNonNull(apiResponse).getRates());
      Object obj = JSONValue.parse(jsonString);
      jsonObject = (JSONObject) obj;
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Exception occurs while processing Json.");
    }

    double fromCurrency = 0;
    double toCurrency = 0;
    try {
      fromCurrency = (Double) jsonObject.get(from.toUpperCase());
      toCurrency = (Double) jsonObject.get(to.toUpperCase());
    } catch (NullPointerException e) {
      log.error("Invalid currency pairs :",e);
      throw new InvalidCurrencyException("Invalid currency pairs. The currency from or two is invalid.");
    }

    double fromCurrencyForgivenCurrency = toCurrency / fromCurrency;
    log.info("fromCurrencyForgivenCurrency : {}",fromCurrencyForgivenCurrency);
    return PairRateResponse.builder()
        .query(getQuery(from, to, amountToCalculate))
        .rate(fromCurrencyForgivenCurrency)
        .totalAmount(fromCurrencyForgivenCurrency * amountToCalculate)
        .build();

  }

  private Query getQuery(String from, String to,
      double amount) {
    return Query.builder()
        .from(from)
        .to(to)
        .amount(amount)
        .build();
  }
}
