package batch;

import java.util.Collection;

public interface CollectionConsumer<V> {
    void accept(Collection<V> c);
}
