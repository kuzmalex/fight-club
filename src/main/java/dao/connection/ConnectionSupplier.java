package dao.connection;

import java.sql.Connection;

public interface ConnectionSupplier {
    Connection get();
}
