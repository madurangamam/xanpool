
package com.xanpool.assessment.rest.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Builder;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "success",
    "query",
    "rate",
    "totalAmount",
    "date"
})

@Builder
@Getter
public class PairRateResponse {

    @JsonProperty("query")
    private Query query;
    @JsonProperty("rate")
    private Double rate;
    @JsonProperty("totalAmount")
    private Double totalAmount;


}
