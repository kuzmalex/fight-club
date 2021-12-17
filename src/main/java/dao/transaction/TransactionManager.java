package dao.transaction;

import java.sql.Connection;
import java.util.function.Consumer;

public interface TransactionManager {
    void begin();
    void commit();
    void execute(Consumer<Connection> callback);
}
