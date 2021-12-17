package dao.impl;

import dao.RoleDao;
import dao.exception.DataAccessException;
import dao.mapper.ResultSetMapper;
import dao.transaction.TransactionManager;
import domain.Role;
import domain.RoleName;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class RoleDaoImpl implements RoleDao {

    private final TransactionManager transactionManager;
    private final ResultSetMapper resultSetMapper;

    public RoleDaoImpl(TransactionManager transactionManager, ResultSetMapper resultSetMapper) {
        this.transactionManager = transactionManager;
        this.resultSetMapper = resultSetMapper;
    }

    @Override
    public Optional<Role> findByName(RoleName name) {
        var role = new AtomicReference<Optional<Role>>();

        transactionManager.execute(connection -> {
            try(PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM role WHERE name = ?"
            )){
                statement.setString(1, name.toString());

                try(ResultSet resultSet = statement.executeQuery()) {
                    role.set(resultSetMapper.map(resultSet, Role.class));
                }
            } catch (SQLException e) {
                throw new DataAccessException(e);
            }
        });

        return role.get();
    }

    @Override
    public void create(Role value) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public List<Role> findAll() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void update(Role value) {
        throw new RuntimeException("Not implemented");
    }
}
