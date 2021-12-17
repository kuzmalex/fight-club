package dao.connection.pool;

import dao.connection.ConnectionSupplier;
import dao.exception.DataAccessException;
import infostructure.di.annotations.AfterContextInitialization;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Exchanger;
import java.util.concurrent.atomic.AtomicInteger;

public class ConnectionPoolImpl implements ConnectionPool{

    private final int maxPoolSize;
    private final int connectionTimeout;
    private final int initialConnectionNumber;

    private final ConnectionSupplier connectionSupplier;

    private final ConcurrentLinkedDeque<Connection> connections = new ConcurrentLinkedDeque<>();
    private final ConcurrentLinkedDeque<Exchanger<Connection>> threadQueue = new ConcurrentLinkedDeque<>();
    private final AtomicInteger poolSize = new AtomicInteger();
    private final AtomicInteger threadQueueSize = new AtomicInteger();

    public ConnectionPoolImpl(
            ConnectionSupplier connectionSupplier,
            int maxPoolSize,
            int initialConnectionNumber,
            int connectionTimeout
    ) {
        this.maxPoolSize = maxPoolSize;
        this.initialConnectionNumber = initialConnectionNumber;
        this.connectionTimeout = connectionTimeout;
        this.connectionSupplier = connectionSupplier;
    }

    @AfterContextInitialization
    private void init(){
        for (int number = 0; number < initialConnectionNumber; number++){
            connections.add(createConnection());
        }
    }

    @Override
    public Connection getConnection() {
        Connection connection = connections.poll();

        if (connection == null) {
            if (poolSize.get() < maxPoolSize) {
                int incrementedSize = poolSize.incrementAndGet();
                if (incrementedSize <= maxPoolSize) {
                    connection = createConnection();
                } else {
                    poolSize.decrementAndGet();
                }
            }
        }

        if (connection == null){
            Exchanger<Connection> exchanger = new Exchanger<>();
            threadQueue.add(exchanger);
            threadQueueSize.incrementAndGet();
            try {
                connection = exchanger.exchange(null);
            } catch (InterruptedException e) {
                throw  new RuntimeException(e);
            }
        }

        try {
            if (!connection.isValid(connectionTimeout)){
                connection = createConnection();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }

        return connection;
    }

    @Override
    public void releaseConnection(Connection connection) {
        Exchanger<Connection> queuedThread = threadQueue.poll();
        if (queuedThread != null){
            threadQueueSize.decrementAndGet();
            try {
                queuedThread.exchange(connection);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        else connections.add(connection);
    }

    private Connection createConnection(){
        return connectionSupplier.get();
    }
}
