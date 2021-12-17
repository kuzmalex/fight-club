import batch.BatchConsumerImpl;
import batch.CollectionConsumer;
import batch.ScheduledBatchConsumer;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

public class ScheduledBatchConsumerTest {

    private final int flushThreshold = Integer.MAX_VALUE;
    private final int maxCapacity = Integer.MAX_VALUE;
    private final int retryTimeOut = 1000;
    private final int period = 1000;

    ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    @Test
    public void shouldInvokeConsumerExpectedNumberTimes() throws InterruptedException {

        AtomicInteger counter = new AtomicInteger();

        CollectionConsumer<Integer> consumer = collection -> {
            counter.incrementAndGet();
        };

        ScheduledBatchConsumer<Integer> batchConsumer = new ScheduledBatchConsumer<>(
                new BatchConsumerImpl<>(
                        consumer,
                        ForkJoinPool.commonPool(),
                        flushThreshold,
                        maxCapacity,
                        retryTimeOut
                ),
                scheduler,
                period
        );

        long timestampStarted = System.currentTimeMillis();
        int delay = 9;
        for (int i = 0; i < 500; i++){
            Thread.sleep(delay);
            batchConsumer.accept(0);
        }

        int count = counter.get();
        long timeStampFinished = System.currentTimeMillis();
        while (count != counter.get()){
            count = counter.get();
            timeStampFinished = System.currentTimeMillis();
        }

        long calculatedCount = (timeStampFinished - timestampStarted) / period;

        Assert.assertEquals(calculatedCount, count);
    }
}