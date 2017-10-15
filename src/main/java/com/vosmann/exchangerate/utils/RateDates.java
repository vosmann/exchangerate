package com.vosmann.exchangerate.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

import static java.util.Optional.empty;

/**
 * Defines the date format that is to be used in the whole application. Helps with using String dates.
 */
public class RateDates {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static boolean isValidPeriod(String startDate, String endDate) {
        final Optional<LocalDate> start = toLocalDate(startDate);
        final Optional<LocalDate> end = toLocalDate(endDate);

        if (start.isPresent() && end.isPresent() && isLessOrEqual(startDate, endDate)) {
            return true;
        }

        return false;
    }

    public static Optional<LocalDate> toLocalDate(String date) {
        try {
            return Optional.of(LocalDate.parse(date, FORMATTER));
        } catch (DateTimeParseException e) {
            return empty();
        }
    }

    public static boolean isLessOrEqual(String start, String end) {
        return start.compareTo(end) <= 0;
    }

}
