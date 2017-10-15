package com.vosmann.exchangerate;

import com.vosmann.exchangerate.utils.RateDates;

import java.util.Objects;

/**
 * Exchange rate class specialized for keeping the USD value of one Euro.
 */
public class Rate {

    private final String timestamp;
    private final float dollarsForEuro;

    public Rate(String timestamp, float dollarsForEuro) {
        if (!RateDates.toLocalDate(timestamp).isPresent()) {
            throw new IllegalArgumentException("Invalid timestamp format for Rate: " + timestamp);
        }
        this.timestamp = timestamp;
        this.dollarsForEuro = dollarsForEuro;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public float getDollarsForEuro() {
        return dollarsForEuro;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rate rate = (Rate) o;
        return Float.compare(rate.dollarsForEuro, dollarsForEuro) == 0 &&
                Objects.equals(timestamp, rate.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, dollarsForEuro);
    }

    @Override
    public String toString() {
        return "Rate{" +
                "timestamp='" + timestamp + '\'' +
                ", dollarsForEuro=" + dollarsForEuro +
                '}';
    }
}
