package config.dao;

import dao.connection.ConnectionSupplier;
import dao.connection.MetricHandlerConnectionSupplier;
import dao.connection.pool.ConnectionPool;
import dao.connection.pool.ConnectionPoolImpl;
import infostructure.di.annotations.Configuration;
import infostructure.di.annotations.ManagedObject;
import server.RequestContext;

import javax.sql.DataSource;

@Configuration
public class ConnectionPoolConfig {

    final int MAX_CONNECTION_POOL_SIZE = Runtime.getRuntime().availableProcessors();
    final int INITIAL_CONNECTION_POOL_SIZE = 1;
    final int CONNECTION_TIMEOUT = 1;

    @ManagedObject
    public ConnectionPool connectionPool(ConnectionSupplier connectionSupplier){
        return new ConnectionPoolImpl(
                connectionSupplier,
                MAX_CONNECTION_POOL_SIZE,
                INITIAL_CONNECTION_POOL_SIZE,
                CONNECTION_TIMEOUT
        );
    }

    @ManagedObject
    public ConnectionSupplier connectionSupplier(DataSource dataSource, RequestContext requestContext){
        return new MetricHandlerConnectionSupplier(dataSource, requestContext);
    }
}
