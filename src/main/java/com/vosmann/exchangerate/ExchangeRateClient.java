package com.vosmann.exchangerate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.Response;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static java.util.Optional.empty;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Client tailored for use with api.fixer.io only.
 */
public class ExchangeRateClient {

    private static final Logger LOG = LogManager.getLogger(ExchangeRateClient.class);

    // Provides three decimal positions.
    private static final String CURRENCY_API = "http://api.fixer.io/latest?symbols=EUR,USD";

    private final AsyncHttpClient client;
    private final ObjectMapper objectMapper;

    public ExchangeRateClient(ObjectMapper objectMapper) {
        this.client = new DefaultAsyncHttpClient();
        this.objectMapper = objectMapper;
    }

    public Optional<Rate> getRate() {

        Response response = getResponse();

        if (response == null) {
            return empty();
        }

        if (response.getStatusCode() != 200) {
            LOG.warn("Could not retrieve exchange rate from remote service at {}. Code: {}",
                     CURRENCY_API, response.getStatusCode());
            return empty();
        }

        return Optional.ofNullable(extractRate(response.getResponseBody()));
    }

    private Response getResponse() {
        try {
            return client.prepareGet(CURRENCY_API)
                         .execute()
                         .get(5, SECONDS); // perform a blocking call
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            LOG.warn("Could not retrieve exchange rate from remote service at {}.", CURRENCY_API, e.getMessage());
            return null;
        }
    }

    private Rate extractRate(String body) {
        try {
            JsonNode json = objectMapper.readTree(body);
            // hacky json
            float dollarsForEuro = json.get("rates")
                                       .get("USD")
                                       .floatValue();
            String date = json.get("date")
                              .textValue();
            return new Rate(date, dollarsForEuro);
        } catch (IOException | NumberFormatException | NullPointerException e) {
            LOG.error("Could not deserialize response. Body: {}", body, e);
            return null;
        }
    }

}
