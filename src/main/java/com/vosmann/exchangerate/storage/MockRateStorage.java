package com.vosmann.exchangerate.storage;

import com.vosmann.exchangerate.Rate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.time.Period;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static com.vosmann.exchangerate.utils.ExchangeRateDates.toLocalDate;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Collections.unmodifiableList;

public class MockRateStorage implements RateStorage {

    private static final Logger LOG = LogManager.getLogger(MockRateStorage.class);

    private static final int MAX_SIZE = 1000;
    private final LinkedList<Rate> rates = new LinkedList<>();

    @Override
    public synchronized void store(Rate rate) {
        rates.add(rate);
        while (rates.size() > MAX_SIZE) {
            rates.removeFirst();
        }
    }

    @Override
    public List<Rate> latestRate() {
        return singletonList(rates.getLast());
    }

    @Override
    public List<Rate> ratesIn(Period period) {
        if (period == null || period.isZero() || period.isNegative()) {
            LOG.warn("Incorrect range provided: {}", period);
            return emptyList();
        }

        rates.stream()
             .filter(rate -)
             .filter(rate -> rate.getTimestamp().)
        return unmodifiableList(rates);
    }

    private static boolean isInPeriod(Period period, Rate rate) {
        Optional<LocalDate> date = toLocalDate(rate.getTimestamp());
        if (date.isPresent() && period.)
    }

}
