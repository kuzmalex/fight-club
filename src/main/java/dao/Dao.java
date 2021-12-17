package dao;

import java.util.List;

public interface Dao<V> {
    void create(V value);
    List<V> findAll();
    void update(V value);
}
