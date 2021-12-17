package config.batch;

import batch.*;
import infostructure.di.annotations.Configuration;
import infostructure.di.annotations.ManagedObject;
import infostructure.di.annotations.Value;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
public class BatchConfiguration {

    @ManagedObject
    public BatchConsumerFactory batchConsumerFactory(
            ScheduledExecutorService taskScheduler,
            @Value("batch_flushThreshold")String flushThresholdValue,
            @Value("batch_period")String periodValue,
            @Value("batch_maxCapacity")String maxCapacityValue,
            @Value("batch_retryTimeOut")String retryTimeOutValue
    ){
        ExecutorService executorService = Executors.newCachedThreadPool();

        int flushThreshold = Integer.parseInt(flushThresholdValue);
        int period = Integer.parseInt(periodValue);
        int maxCapacity = Integer.parseInt(maxCapacityValue);
        int retryTimeOut = Integer.parseInt(retryTimeOutValue);

        return new BatchConsumerFactory() {
            @Override
            public <V> ScheduledBatchConsumer<V> newScheduledBatchConsumer(CollectionConsumer<V> collectionConsumer) {
                return new ScheduledBatchConsumer<>(
                        batchConsumer(collectionConsumer, executorService, flushThreshold, maxCapacity, retryTimeOut),
                        taskScheduler,
                        period
                );
            }
            @Override
            public <V> BatchConsumer<V> newBatchConsumer(CollectionConsumer<V> collectionConsumer) {
                return batchConsumer(collectionConsumer, executorService, flushThreshold, maxCapacity, retryTimeOut);
            }
        };
    }

    private <V> BatchConsumer<V> batchConsumer(
            CollectionConsumer<V> collectionConsumer,
            ExecutorService executorService,
            int flushThreshold,
            int maxCapacity,
            int retryTimeOut
    ) {
        return new BatchConsumerImpl<>(
                collectionConsumer,
                executorService,
                flushThreshold,
                maxCapacity,
                retryTimeOut
        );
    }
}
