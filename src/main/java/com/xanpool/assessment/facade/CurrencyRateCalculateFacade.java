package com.xanpool.assessment.facade;

import java.util.concurrent.ExecutionException;

import com.xanpool.assessment.rest.model.PairRateResponse;

public interface CurrencyRateCalculateFacade {

  PairRateResponse calculateCurrencyRate(String accessKey,String from ,String to,String amount)
      throws ExecutionException;

}
