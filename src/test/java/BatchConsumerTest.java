import batch.BatchConsumer;
import batch.BatchConsumerImpl;
import batch.CollectionConsumer;
import batch.exception.OutOfConsumerCapacityException;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class BatchConsumerTest {

    private final int flushThreshold = 1000;
    private final int maxCapacity = 10000;
    private final int retryTimeOut = 1000;

    ExecutorService executorService = ForkJoinPool.commonPool();

    @Test
    public void shouldNotConsumeWithoutFlush() throws Exception{

        int valuesNumber = 2;
        CountDownLatch lock = new CountDownLatch(valuesNumber);

        AtomicInteger summ = new AtomicInteger();
        CollectionConsumer<Integer> consumer = collection -> {
            for (Integer value : collection) {
                summ.addAndGet(value);
                lock.countDown();
            }
        };

        BatchConsumer<Integer> batchConsumer = createBatchConsumer(consumer);
        batchConsumer.accept(1);
        batchConsumer.accept(2);

        lock.await(3, TimeUnit.SECONDS);

        Assert.assertEquals(0, summ.get());
    }

    @Test
    public void shouldConsumeAfterFlush() throws Exception{

        int valuesNumber = 2;
        CountDownLatch lock = new CountDownLatch(valuesNumber);

        AtomicInteger summ = new AtomicInteger();
        CollectionConsumer<Integer> consumer = c -> {
            for (Integer value : c){
                summ.addAndGet(value);
                lock.countDown();
            }
        };

        BatchConsumer<Integer> batchConsumer = createBatchConsumer(consumer);
        batchConsumer.accept(1);
        batchConsumer.accept(2);

        Assert.assertEquals(0, summ.get());

        batchConsumer.flush();
        lock.await(3, TimeUnit.SECONDS);

        Assert.assertEquals(3, summ.get());
    }

    @Test
    public void shouldConsumeAfterFlushThresholdIsReached() throws Exception{

        int valuesNumber = flushThreshold;
        CountDownLatch lock = new CountDownLatch(valuesNumber);

        AtomicInteger summ = new AtomicInteger();
        CollectionConsumer<Integer> consumer = c -> {
            for (Integer value : c){
                summ.addAndGet(value);
                lock.countDown();
            }
        };

        BatchConsumer<Integer> batchConsumer = createBatchConsumer(consumer);

        for (int i = 1; i < flushThreshold; i++){
            batchConsumer.accept(i);
        }
        Assert.assertEquals(summ.get(), 0);

        batchConsumer.accept(flushThreshold);
        lock.await(3, TimeUnit.SECONDS);

        int expectedSumm = flushThreshold * (flushThreshold + 1) / 2;
        Assert.assertEquals(expectedSumm, summ.get());
    }

    @Test
    public void shouldConsumeCollectionOfValues() throws Exception{
        Collection<Integer> values = Arrays.asList(1, 2);

        int valuesNumber = values.size();
        CountDownLatch lock = new CountDownLatch(valuesNumber);

        AtomicInteger summ = new AtomicInteger();
        CollectionConsumer<Integer> consumer = collection -> {
            for (Integer value : collection) {
                summ.addAndGet(value);
                lock.countDown();
            }
        };

        BatchConsumer<Integer> batchConsumer = createBatchConsumer(consumer);
        batchConsumer.accept(values);

        batchConsumer.flush();
        lock.await(3, TimeUnit.SECONDS);

        Assert.assertEquals(3, summ.get());
    }

    @Test
    public void shouldRetryOnErrors() throws Exception{
        AtomicInteger invocationCount = new AtomicInteger();
        CountDownLatch lock = new CountDownLatch(1);
        CollectionConsumer<Integer> consumer = collection -> {
            invocationCount.incrementAndGet();
            if (invocationCount.get() < 3)
                throw new RuntimeException();
            lock.countDown();
        };

        BatchConsumer<Integer> batchConsumer = createBatchConsumer(consumer);
        batchConsumer.accept(Collections.singleton(1));

        batchConsumer.flush();
        lock.await(retryTimeOut * 3, TimeUnit.MILLISECONDS);

        Assert.assertEquals(3, invocationCount.get());
    }

    @Test
    public void shouldThrowOutOfCapacityException() throws Exception{
        Collection<Integer> values = new ArrayList<>();
        for (int i = 0; i < maxCapacity + 1; i++){
            values.add(i);
        }

        CollectionConsumer<Integer> consumer = collection -> {};
        BatchConsumer<Integer> batchConsumer = createBatchConsumer(consumer);

        Assert.assertThrows(
                OutOfConsumerCapacityException.class,
                ()->batchConsumer.accept(values)
        );
    }

    <T> BatchConsumer<T> createBatchConsumer(CollectionConsumer<T> collectionConsumer){
        return new BatchConsumerImpl<>(
                collectionConsumer,
                executorService,
                flushThreshold,
                maxCapacity,
                retryTimeOut
        );
    }
}
