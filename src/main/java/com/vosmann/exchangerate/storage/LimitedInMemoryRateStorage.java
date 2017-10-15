package com.vosmann.exchangerate.storage;

import com.vosmann.exchangerate.Rate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;
import java.util.List;

import static com.vosmann.exchangerate.utils.RateDates.isLessOrEqual;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

public class LimitedInMemoryRateStorage implements RateStorage {

    private static final Logger LOG = LogManager.getLogger(LimitedInMemoryRateStorage.class);

    private final int maxSize;
    private final LinkedList<Rate> rates;

    public LimitedInMemoryRateStorage(int maxSize) {
        this.maxSize = maxSize;
        this.rates = new LinkedList<>();
    }

    @Override
    public synchronized void store(Rate rate) {
        if (!rates.isEmpty() && rates.getLast().equals(rate)) {
            LOG.info("Not storing identical exchange rate for same timestamp: {}", rate);
            return;
        }

        rates.add(rate);
        while (rates.size() > maxSize) {
            rates.removeFirst();
        }
    }

    @Override
    public List<Rate> latestRate() {
        if (rates.isEmpty()) {
            return emptyList();
        } else {
            return singletonList(rates.getLast());
        }
    }

    @Override
    public List<Rate> ratesIn(String startDate, String endDate) {
        return unmodifiableList(rates.stream()
                                     .filter(rate -> isLessOrEqual(startDate, rate.getTimestamp()))
                                     .filter(rate -> isLessOrEqual(rate.getTimestamp(), endDate))
                                     .collect(toList()));
    }

    public int size() {
        return rates.size();
    }

}
