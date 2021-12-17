package config.dao;

import dao.connection.pool.ConnectionPool;
import dao.transaction.TransactionManager;
import dao.transaction.TransactionManagerImpl;
import infostructure.di.annotations.Configuration;
import infostructure.di.annotations.ManagedObject;

@Configuration
public class TransactionManagerConfig {

    @ManagedObject
    public TransactionManager transactionManager(ConnectionPool connectionPool){
        return new TransactionManagerImpl(connectionPool);
    }
}
