package com.vosmann.exchangerate.storage;

import com.vosmann.exchangerate.Rate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;
import java.util.List;

import static com.vosmann.exchangerate.utils.ExchangeRateDates.isLessOrEqual;
import static java.util.Collections.singletonList;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

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
    public List<Rate> ratesIn(String startDate, String endDate) {
        return unmodifiableList(rates.stream()
                                     .filter(rate -> isLessOrEqual(startDate, rate.getTimestamp()))
                                     .filter(rate -> isLessOrEqual(rate.getTimestamp(), endDate))
                                     .collect(toList()));
    }

}
