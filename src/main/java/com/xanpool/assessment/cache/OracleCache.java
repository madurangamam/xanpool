package com.xanpool.assessment.cache;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.xanpool.assessment.rest.client.CurrencyRateRestClient;
import com.xanpool.assessment.rest.model.ApiResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OracleCache {

  private final CurrencyRateRestClient currencyRateRestClient;

  LoadingCache<String, ApiResponse> rateCache =
      CacheBuilder.newBuilder()
          .maximumSize(1000)                             // maximum 1000 records can be cached
          .expireAfterAccess(60,
              TimeUnit.MINUTES)      // cache will expire after 60 minutes of access
          .build(new CacheLoader<String, ApiResponse>() {

            @Override
            public ApiResponse load(String accessKey) throws Exception {
              return getFromService(accessKey);
            }
          });

  private ApiResponse getFromService(String accessKey) {
    return currencyRateRestClient.getRates(accessKey).getBody();
  }

  public ApiResponse getCache(String cacheRef) throws ExecutionException {
    ApiResponse apiResponse = null;
    apiResponse = rateCache.get(cacheRef);
    return apiResponse;
  }

}
