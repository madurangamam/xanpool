package com.xanpool.assessment.exception;

import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR;
import static org.springframework.http.HttpStatus.Series.SERVER_ERROR;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.ResponseErrorHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.CharStreams;
import com.xanpool.assessment.rest.model.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

  @Override
  public boolean hasError(ClientHttpResponse httpResponse) throws IOException {

    return (httpResponse.getStatusCode().series() == CLIENT_ERROR
        || httpResponse.getStatusCode().series() == SERVER_ERROR);
  }

  @Override
  public void handleError(ClientHttpResponse httpResponse) throws IOException {
    String response = toString(httpResponse.getBody());
    log.error(response);

    if (httpResponse.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR) {
      throw new HttpClientServerResponseException(
          getErrorMessage(response, httpResponse.getStatusCode()));
    } else if (httpResponse.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR) {
      if (httpResponse.getStatusCode() == HttpStatus.BAD_REQUEST) {
        throw new BadRequestException(getErrorMessage(response, httpResponse.getStatusCode()));
      }
    }
  }

  private String toString(InputStream inputStream) throws IOException {
    return CharStreams.toString(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
        .replace("[\\n]", "");
  }

  private String getErrorMessage(final String response, final HttpStatus statusCode) {

    final ErrorResponse errorResponse;
   final String message = "Status-"
        .concat(statusCode.toString())
        .concat("\n message- [");
    try {
      errorResponse = new ObjectMapper().readValue(response, ErrorResponse.class);

      return message
          .concat(errorResponse.getError())
          .concat("].");

    } catch (JsonProcessingException e) {
      log.warn("Unable to convert Http response {} to json", response, e);
      if (StringUtils.hasText(statusCode.toString())) {
        return message
            .concat(statusCode +"]");
      }
      return "message-Unknown exception occurred.";
    }
  }
}
