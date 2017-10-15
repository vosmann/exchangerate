package com.vosmann.exchangerate;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.vosmann.exchangerate.storage.RateStorage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.util.Collections.emptyList;
import static java.util.concurrent.TimeUnit.MINUTES;

/**
 * Serves three purposes: 1. periodically loads the newest exchange rate from the remote service into local storage (DB)
 * 2. interfaces with RateStorage, i.e. the DB and 3. caches the most common call: the one for latest rate.
 */
public class RateService {

    private static final Logger LOG = LogManager.getLogger(RateService.class);

    private static final long INIT_DELAY = 0;
    private static final String LATEST_RATE_CACHE_KEY = "lr";
    private static final long CACHE_REFRESH_PERIOD_IN_SECONDS = 10;
    private static final long DEFAULT_CHECK_PERIOD_IN_MINUTES = 5;

    private final RateStorage storage;
    private final RateClient client;
    private final long checkIntervalInMin;
    private final ScheduledExecutorService rateCheckExecutor;
    private final Cache<String, List<Rate>> cache;

    public RateService(RateStorage rateStorage, RateClient rateClient, int delayInMin) {
        storage = rateStorage;
        client = rateClient;
        checkIntervalInMin = sanitizeCheckInterval(delayInMin);

        rateCheckExecutor = Executors.newSingleThreadScheduledExecutor();
        rateCheckExecutor.scheduleWithFixedDelay(this::check,
                                                 INIT_DELAY, checkIntervalInMin, MINUTES);

        cache = CacheBuilder.newBuilder()
                            .expireAfterWrite(CACHE_REFRESH_PERIOD_IN_SECONDS, TimeUnit.SECONDS)
                            .initialCapacity(1)
                            .maximumSize(1)
                            .build();
    }

    public List<Rate> getLatest() {
        try {
            return cache.get(LATEST_RATE_CACHE_KEY,
                             () -> storage.latestRate());
        } catch (Exception e) {
            LOG.error("Could not load latest value from storage into cache.");
            return emptyList();
        }
    }

    public List<Rate> getFrom(String startDate, String endDate) {
        return storage.ratesIn(startDate, endDate);
    }

    private void check() {
        Optional<Rate> rate = client.getRate();
        if (rate.isPresent()) {
            storage.store(rate.get());
        }
    }

    static class LatestRateLoader extends CacheLoader<String, List<Rate>> {

        private final RateStorage storage;

        public LatestRateLoader(RateStorage storage) {
            this.storage = storage;
        }

        @Override
        public List<Rate> load(String key) throws Exception {
            return storage.latestRate();
        }
    }

    private long sanitizeCheckInterval(long interval) {
        if (interval < 1) {
            LOG.warn("Interval lower than 1 minute. Falling back to default of {} minutes.",
                     DEFAULT_CHECK_PERIOD_IN_MINUTES);
            return DEFAULT_CHECK_PERIOD_IN_MINUTES;
        }

        return interval;
    }

}
