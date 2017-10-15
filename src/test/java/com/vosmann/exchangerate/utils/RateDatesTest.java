package com.vosmann.exchangerate.utils;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RateDatesTest {

    @Test
    public void testValidPeriod() {
        assertTrue(RateDates.isValidPeriod("2016-06-21", "2016-06-24"));
    }

    @Test
    public void testValidSingleDayPeriod() {
        assertTrue(RateDates.isValidPeriod("2016-06-21", "2016-06-21"));
    }

    @Test
    public void testInvalidPeriod() {
        assertFalse(RateDates.isValidPeriod("2016-06-21", "2016-06-20"));
    }

    @Test
    public void testInvalidStartDate() {
        assertFalse(RateDates.isValidPeriod("2016-06-lol", "2016-06-20"));
    }

    @Test
    public void testInvalidEndDate() {
        assertFalse(RateDates.isValidPeriod("2016-06-20", "2016-lol-20"));
    }

    @Test
    public void testIsLessOrEqual() {
        assertTrue(RateDates.isLessOrEqual("2016-06-21", "2016-06-21"));
        assertTrue(RateDates.isLessOrEqual("2016-06-21", "2016-06-22"));
    }

    @Test
    public void testIsNotLessOrEqual() {
        assertFalse(RateDates.isLessOrEqual("2016-06-24", "2016-06-21"));
    }
}