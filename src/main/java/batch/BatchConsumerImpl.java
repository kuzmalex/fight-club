package batch;

import batch.exception.OutOfConsumerCapacityException;

import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

public class BatchConsumerImpl<V> implements BatchConsumer<V> {

    private final int flushThreshold;
    private final int maxCapacity;
    private final int retryTimeOut;
    private final AtomicInteger totalValuesInProcessing = new AtomicInteger();

    private final CollectionConsumer<V> collectionConsumer;
    private final ExecutorService executor;

    private final Collection<V> buffer = ConcurrentHashMap.newKeySet();

    private final Object flushConditionCheckLock = new Object();

    public BatchConsumerImpl(CollectionConsumer<V> collectionConsumer, ExecutorService executor,
                             int flushThreshold, int maxCapacity, int retryTimeOut) {
        this.collectionConsumer = collectionConsumer;
        this.executor = executor;
        this.flushThreshold = flushThreshold;
        this.maxCapacity = maxCapacity;
        this.retryTimeOut = retryTimeOut;
    }

    @Override
    public void accept(V value){

        if (!canAcceptValue()){
            throw new OutOfConsumerCapacityException(totalValuesInProcessing.get(), maxCapacity);
        }

        totalValuesInProcessing.incrementAndGet();
        addValue(value);
    }

    @Override
    public void accept(Collection<V> c) {

        if (!canAcceptCollection(c)){
            throw new OutOfConsumerCapacityException(
                    totalValuesInProcessing.get(),
                    c.size(),
                    maxCapacity
            );
        }

        totalValuesInProcessing.addAndGet(c.size());
        c.forEach(this::addValue);
    }

    private void addValue(V value){
        buffer.add(value);

        if (flushCondition()){
            synchronized (flushConditionCheckLock){
                if (flushCondition()){
                    flush();
                }
            }
        }
    }

    public void flush(){
        Collection<V> valueBulk = new HashSet<>();
        for (V value : buffer){
            valueBulk.add(value);
            buffer.remove(value);
        }

        executor.submit(()->processBulk(valueBulk));
    }

    private boolean flushCondition(){
        return buffer.size() >= flushThreshold;
    }

    private boolean canAcceptValue(){
        return totalValuesInProcessing.get() < maxCapacity;
    }

    private boolean canAcceptCollection(Collection<V> c) {
        return totalValuesInProcessing.get() + c.size() < maxCapacity;
    }

    private void processBulk(Collection<V> values){
        if (values.isEmpty()) return;

        try {
            collectionConsumer.accept(values);
        }
        catch (Exception e){
            retry(values);
            throw new RuntimeException(e);
        }
        totalValuesInProcessing.addAndGet(-1 * values.size());
    }

    private void retry(Collection<V> values){
        executor.submit(()->{
            try {
                Thread.sleep(retryTimeOut);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            processBulk(values);
        });
    }
}
