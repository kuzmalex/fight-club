package batch;

public interface BatchConsumerFactory {
    <V> ScheduledBatchConsumer<V> newScheduledBatchConsumer(CollectionConsumer<V> collectionConsumer);
    <V> BatchConsumer<V> newBatchConsumer(CollectionConsumer<V> collectionConsumer);
}
