package batch;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class ScheduledBatchConsumer<V> implements Consumer<V> {

    private final BatchConsumer<V> batchConsumer;

    public ScheduledBatchConsumer(BatchConsumer<V> batchConsumer, ScheduledExecutorService scheduler, int period){
        this.batchConsumer = batchConsumer;
        scheduler.scheduleAtFixedRate(batchConsumer::flush, 0, period, TimeUnit.MILLISECONDS);
    }

    @Override
    public void accept(V value) {
        batchConsumer.accept(value);
    }
}