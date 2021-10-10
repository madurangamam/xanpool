package com.xanpool.assessment.cache;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.xanpool.assessment.facade.CurrencyRateCalculateFacade;
import com.xanpool.assessment.rest.model.CacheReference;
import com.xanpool.assessment.rest.model.PairRateResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExchangeRateCache {

  private final CurrencyRateCalculateFacade currencyRateCalculateFacade;

  LoadingCache<CacheReference, PairRateResponse> authorCache =
      CacheBuilder.newBuilder()
          .maximumSize(1000)                             // maximum 1000 records can be cached
          .expireAfterAccess(60,
              TimeUnit.MINUTES)      // cache will expire after 60 minutes of access
          .build(new CacheLoader<CacheReference, PairRateResponse>() {

            @Override
            public PairRateResponse load(CacheReference cacheReference) throws Exception {
              return getFromFacade(cacheReference);
            }
          });


  private PairRateResponse getFromFacade(CacheReference cacheReference) throws ExecutionException {
    return currencyRateCalculateFacade.calculateCurrencyRate(cacheReference.getAccessKey(),
        cacheReference.getFromCurrency(), cacheReference.getToCurrency(),
        cacheReference.getAmount());
  }

  public PairRateResponse getCache(CacheReference cacheReference) throws ExecutionException {
    PairRateResponse pairRateResponse = null;
    pairRateResponse = authorCache.get(cacheReference);
    return pairRateResponse;
  }

}
