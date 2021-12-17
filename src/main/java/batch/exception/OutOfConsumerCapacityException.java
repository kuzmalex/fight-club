package batch.exception;

public class OutOfConsumerCapacityException extends RuntimeException {
    public OutOfConsumerCapacityException(int totalValues, int maxCapacity){
        super("Consumer is out of capacity: " +
                totalValues + " values are being processed" +
                " consumer capacity is " + maxCapacity
        );
    }
    public OutOfConsumerCapacityException(int totalValues, int collectionSize, int maxCapacity){
        super(
                "Consumer cannot accept collection: " +
                        totalValues +
                        " values are being processed" +
                        " collection size is " + collectionSize +
                        " consumer capacity is " + maxCapacity
        );
    }
}
