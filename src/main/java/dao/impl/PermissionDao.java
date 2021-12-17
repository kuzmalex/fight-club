package dao.impl;

import dao.Dao;
import dao.exception.DataAccessException;
import dao.mapper.ResultSetMapper;
import dao.transaction.TransactionManager;
import domain.Permission;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class PermissionDao implements Dao<Permission> {

    private final TransactionManager transactionManager;
    private final ResultSetMapper resultSetMapper;

    public PermissionDao(TransactionManager transactionManager, ResultSetMapper resultSetMapper) {
        this.transactionManager = transactionManager;
        this.resultSetMapper = resultSetMapper;
    }

    @Override
    public void create(Permission value) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public List<Permission> findAll() {
        var users = new AtomicReference<List<Permission>>();

        transactionManager.execute(connection -> {
            try(
                    PreparedStatement statement = connection.prepareStatement(
                            "SELECT * FROM permission"
                    );
                    ResultSet resultSet = statement.executeQuery()
            ){
                users.set(resultSetMapper.mapCollection(resultSet, Permission.class));
            } catch (SQLException e) {
                throw new DataAccessException(e);
            }
        });

        return users.get();
    }

    @Override
    public void update(Permission value) {
        throw new RuntimeException("Not implemented");
    }
}
