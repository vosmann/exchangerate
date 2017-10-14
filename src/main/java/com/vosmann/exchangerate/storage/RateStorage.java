package com.vosmann.exchangerate.storage;

import com.vosmann.exchangerate.Rate;

import java.util.List;

public interface RateStorage {

    void store(Rate rate);

    List<Rate> latestRate();

    List<Rate> ratesIn(String startDate, String endDate);

}
