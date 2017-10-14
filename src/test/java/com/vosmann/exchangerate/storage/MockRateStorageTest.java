package com.vosmann.exchangerate.storage;

import org.junit.Test;

import java.util.LinkedList;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class MockRateStorageTest {

    @Test
    public void testList() {
        LinkedList<Integer> list = new LinkedList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        assertThat(list.getLast(), is(3));

        list.removeFirst();
        assertThat(list.getFirst(), is(2));
    }

    @Test
    public void testStore() {
    }

}