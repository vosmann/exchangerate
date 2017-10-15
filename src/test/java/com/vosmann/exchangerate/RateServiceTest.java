package com.vosmann.exchangerate;

import com.vosmann.exchangerate.storage.RateStorage;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RateServiceTest {

    private static final Rate RATE_A = new Rate("2013-05-30", 1.5f);

    @Mock
    private RateClient client;
    @Mock
    private RateStorage storage;

    @Test
    public void testLoadFromClientOnCreation() {
        when(client.getRate()).thenReturn(Optional.of(RATE_A));

        new RateService(storage, client, 10);
        waitMillis(10); // give thread in executor time to call the mocks; ugly

        verify(client, times(1)).getRate();
        verify(storage, times(1)).store(RATE_A);
    }

    @Test
    public void testFirstCallHitsDbSecondHitsCache() {
        when(client.getRate()).thenReturn(Optional.of(RATE_A));
        when(storage.latestRate()).thenReturn(singletonList(RATE_A));

        RateService service = new RateService(storage, client, 10);

        List<Rate> latestRate;
        latestRate = service.getLatest();
        latestRate = service.getLatest(); // two calls

        assertThat(latestRate, is(singletonList(RATE_A)));

        verify(storage, times(1)).latestRate(); // first call was to db; second to cache only
    }

    @Test
    public void testClientFails() {
        when(client.getRate()).thenReturn(Optional.empty());

        new RateService(storage, client, 10);

        verify(client, times(1)).getRate();
        verify(storage, times(0)).store(RATE_A);
    }

    private void waitMillis(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Assert.fail("Could not wait.");
        }
    }

}