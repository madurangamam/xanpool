package com.xanpool.assessment.rest.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.xanpool.assessment.cache.ExchangeRateCache;
import com.xanpool.assessment.rest.model.PairRateResponse;
import com.xanpool.assessment.rest.model.Query;

@RunWith(MockitoJUnitRunner.class)
public class CurrencyConvertControllerTest {

  private MockMvc mvc;
  private String CONTENT_ASSOCIATION_URL = "/api/convert?access_key=649a1b0b0f0a325f910650da84f7a604&from=USD&to=SGD";

  @InjectMocks
  private CurrencyConvertController currencyConvertController;

  @Mock
  private ExchangeRateCache exchangeRateCache;

  @Before
  public void setUp() {
    mvc = MockMvcBuilders.standaloneSetup(currencyConvertController)
        .build();
  }

  @Test
  public void testGetPosts() throws Exception {
    when(exchangeRateCache.getCache(any())).thenReturn(getPost());
    mvc.perform(MockMvcRequestBuilders.get(CONTENT_ASSOCIATION_URL)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  private PairRateResponse getPost(){
    return PairRateResponse.builder()
        .query(getQuery("USD", "SGD", 1.0))
        .rate(1.3554978661644521)
        .totalAmount(1.3554978661644521)
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
