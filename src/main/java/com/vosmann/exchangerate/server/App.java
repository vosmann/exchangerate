package com.vosmann.exchangerate.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vosmann.exchangerate.ExchangeRateClient;
import com.vosmann.exchangerate.ExchangeRateService;
import com.vosmann.exchangerate.storage.MockRateStorage;
import com.vosmann.exchangerate.storage.RateStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class App {

    private static final Logger LOG = LoggerFactory.getLogger(App.class);

    @Bean
    public ExchangeRateService exchangeRateService(RateStorage storage, ExchangeRateClient client) {
        return new ExchangeRateService(storage, client);
    }

    @Bean
    public RateStorage rateStorage() {
        return new MockRateStorage();
    }

    @Bean
    public ExchangeRateClient exchangeRateClient(ObjectMapper objectMapper) {
        return new ExchangeRateClient(objectMapper);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    public static void main(final String[] args) throws Exception {
        SpringApplication.run(App.class, args);
    }

}
