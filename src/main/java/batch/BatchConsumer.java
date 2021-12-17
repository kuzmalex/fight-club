package batch;

import java.util.function.Consumer;

public interface BatchConsumer<V> extends Consumer<V>, CollectionConsumer<V> {
    void flush();
}
