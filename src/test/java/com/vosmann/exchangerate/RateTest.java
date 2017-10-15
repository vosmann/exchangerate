package com.vosmann.exchangerate;

import org.junit.Test;

public class RateTest {

    @Test
    public void testDateOk() {
        new Rate("2016-12-28", 1.0f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDateHasWrongFormat() {
        new Rate("2016-12-28-14-30", 1.0f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDateInvalid() {
        new Rate("not a date", 1.0f);
    }

}