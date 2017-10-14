package com.vosmann.exchangerate.utils;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

import static java.util.Optional.empty;

/**
 * Defines the date format that is to be used in the whole application.
 */
public class ExchangeRateDates {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static Optional<Period> periodBetween(String startDate, String endDate) {
        final Optional<LocalDate> start = toLocalDate(startDate);
        final Optional<LocalDate> end = toLocalDate(endDate);

        if (start.isPresent() && end.isPresent()) {
            return Optional.of(Period.between(start.get(),
                                              end.get().plusDays(1))); // end is exclusive
        }

        return empty();
    }

    public static Optional<LocalDate> toLocalDate(String date) {
        try {
            return Optional.of(LocalDate.parse(date, FORMATTER));
        } catch (DateTimeParseException e) {
            return empty();
        }
    }

}
