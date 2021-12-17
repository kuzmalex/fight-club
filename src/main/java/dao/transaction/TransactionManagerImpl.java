package dao.transaction;

import dao.connection.pool.ConnectionPool;
import dao.exception.DataAccessException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TransactionManagerImpl implements TransactionManager {

    private final ConnectionPool connectionPool;

    private final ThreadLocal<Connection> localConnection = ThreadLocal.withInitial(()->null);
    private final ThreadLocal<List<Consumer<Connection>>> localCallbacks = ThreadLocal.withInitial(()->null);

    private final ThreadLocal<Boolean> beginInvoked = ThreadLocal.withInitial(()->Boolean.FALSE);

    public TransactionManagerImpl(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public void begin(){
        Connection connection = localConnection.get();

        if (connection != null){
            try {
                connection.rollback();
            } catch (SQLException ignored) {}
            connectionPool.releaseConnection(connection);
        }

        connection = connectionPool.getConnection();
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        localConnection.set(connection);
        beginInvoked.set(true);
    }

    public void commit(){
        transact(localConnection.get(), localCallbacks.get());
        localConnection.remove();
        localCallbacks.remove();
        beginInvoked.remove();
    }

    public void execute(Consumer<Connection> callback){
        boolean isManual = beginInvoked.get();
        if (!isManual){
            begin();
        }
        List<Consumer<Connection>> callbacks = localCallbacks.get();
        if (callbacks == null) callbacks = new ArrayList<>();
        localCallbacks.set(callbacks);
        callbacks.add(callback);
        if (!isManual){
            commit();
        }
    }

    private void transact(Connection connection, List<Consumer<Connection>> callbacks) {
        try {
            for (Consumer<Connection> callback : callbacks){
                callback.accept(connection);
            }
            connection.commit();

        } catch (Exception e) {

            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    throw new DataAccessException(e);
                }
            }
            throw (e instanceof DataAccessException
                    ? (DataAccessException) e
                    : new DataAccessException(e));
        }
        finally {
            if(connection != null) {
                connectionPool.releaseConnection(connection);
            }
        }
    }
}
