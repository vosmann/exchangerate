package com.vosmann.exchangerate.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vosmann.exchangerate.RateClient;
import com.vosmann.exchangerate.RateService;
import com.vosmann.exchangerate.storage.LimitedInMemoryRateStorage;
import com.vosmann.exchangerate.storage.RateStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class App {

    private static final Logger LOG = LoggerFactory.getLogger(App.class);

    @Value("${exchangerate.update.checkIntervalInMinutes}")
    private int checkIntervalInMinutes;

    @Bean
    public RateService exchangeRateService(RateStorage storage, RateClient client) {
        return new RateService(storage, client, checkIntervalInMinutes);
    }

    @Bean
    public RateStorage rateStorage() {
        final int maxStorageSize = 1000;
        return new LimitedInMemoryRateStorage(maxStorageSize);
    }

    @Bean
    public RateClient exchangeRateClient(ObjectMapper objectMapper) {
        return new RateClient(objectMapper);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    public static void main(final String[] args) throws Exception {
        SpringApplication.run(App.class, args);
    }

}
