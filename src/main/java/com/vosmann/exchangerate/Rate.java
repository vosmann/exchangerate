package com.vosmann.exchangerate;

/**
 * Exchange rate class specialized for keeping the USD value of one Euro.
 */
public class Rate {

    private final String timestamp;
    private final float dollarsForEuro;

    public Rate(String timestamp, float dollarsForEuro) {
        this.timestamp = timestamp;
        this.dollarsForEuro = dollarsForEuro;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public float getDollarsForEuro() {
        return dollarsForEuro;
    }

}
