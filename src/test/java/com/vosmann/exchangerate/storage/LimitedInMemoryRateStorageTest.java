package com.vosmann.exchangerate.storage;

import com.vosmann.exchangerate.Rate;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;

import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class LimitedInMemoryRateStorageTest {

    @Test
    public void testGetLatest() {
        final int maxSize = 100;
        final LimitedInMemoryRateStorage storage = new LimitedInMemoryRateStorage(maxSize);

        final Rate first = new Rate("2016-10-15", 1.5f);
        final Rate second = new Rate("2016-10-25", 2.5f);
        final Rate third = new Rate("2016-10-30", 3.0f);

        storage.store(first);
        storage.store(second);
        storage.store(third);

        assertThat(storage.latestRate(), is(singletonList(third)));
    }

    @Test
    public void testGetRange() {
        final int maxSize = 100;
        final LimitedInMemoryRateStorage storage = new LimitedInMemoryRateStorage(maxSize);

        for (int day = 10; day <= 20; ++day) {
            storage.store(makeRateMatchingItsDay("1993-12-", day));
        }

        assertThat(storage.ratesIn("1993-12-15", "1993-12-19"),
                   is(Arrays.asList(new Rate("1993-12-15", 15.0f),
                                    new Rate("1993-12-16", 16.0f),
                                    new Rate("1993-12-17", 17.0f),
                                    new Rate("1993-12-18", 18.0f),
                                    new Rate("1993-12-19", 19.0f))));
    }

    @Test
    public void testDontStoreIdentical() {
        final int maxSize = 100;
        final LimitedInMemoryRateStorage storage = new LimitedInMemoryRateStorage(maxSize);

        final Rate rate = new Rate("2016-10-15", 1.5f);

        storage.store(rate);
        storage.store(rate);
        storage.store(rate);

        assertThat(storage.size(), is(1));
    }

    @Test
    public void testSizeLimitation() {
        final int maxSize = 100;
        final LimitedInMemoryRateStorage storage = new LimitedInMemoryRateStorage(maxSize);

        for (int i = 0; i < maxSize + 10; ++i) {
            storage.store(new Rate("2016-10-15", 0.5f + i));
        }

        assertThat(storage.size(), is(maxSize));
    }

    @Test
    public void testSizeLimitationDeletesOldest() {
        final int maxSize = 1;
        final LimitedInMemoryRateStorage storage = new LimitedInMemoryRateStorage(maxSize);

        final Rate first = new Rate("2016-10-15", 1.5f);
        final Rate second = new Rate("2016-10-25", 2.5f);

        storage.store(first);
        storage.store(second);

        assertThat(storage.size(), is(1));
        assertThat(storage.latestRate(), is(singletonList(second)));
    }

    @Test
    public void testListFirstAndLast() {
        LinkedList<Integer> list = new LinkedList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        assertThat(list.getLast(), is(3));

        list.removeFirst();
        assertThat(list.getFirst(), is(2));
    }

    private Rate makeRateMatchingItsDay(String datePrefix, int day) {
        return new Rate(datePrefix + day, day);
    }

}