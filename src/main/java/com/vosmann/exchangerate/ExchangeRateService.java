package com.vosmann.exchangerate;

import com.vosmann.exchangerate.storage.RateStorage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.MINUTES;

public class ExchangeRateService {

    private static final Logger LOG = LogManager.getLogger(ExchangeRateService.class);

    private static final long INIT_DELAY_IN_MIN = 0;
    private static final long CHECK_DELAY_IN_MIN = 2;

    private final RateStorage storage;
    private final ExchangeRateClient client;
    private final ScheduledExecutorService rateCheckExecutor;

    public ExchangeRateService(RateStorage rateStorage, ExchangeRateClient rateClient) {
        storage = rateStorage;
        client = rateClient;

        rateCheckExecutor = Executors.newSingleThreadScheduledExecutor();
        rateCheckExecutor.scheduleWithFixedDelay(this::check,
                                                 INIT_DELAY_IN_MIN, CHECK_DELAY_IN_MIN, MINUTES);
    }

    public List<Rate> getLatest() {
        return storage.latestRate();
    }

    public List<Rate> getFrom(final Period period) {

        return storage.ratesIn(period);
    }

    private void check() {
        Optional<Rate> rate = client.getRate();
        if (rate.isPresent()) {
            storage.store(rate.get());
        }
    }

}
