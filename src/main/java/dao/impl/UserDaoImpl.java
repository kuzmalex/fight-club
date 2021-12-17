package dao.impl;

import dao.UserDao;
import dao.exception.DataAccessException;
import dao.mapper.ResultSetIterator;
import dao.mapper.ResultSetMapper;
import dao.transaction.TransactionManager;
import domain.Permission;
import domain.Role;
import domain.RoleName;
import domain.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class UserDaoImpl implements UserDao {

    private final TransactionManager transactionManager;
    private final ResultSetMapper resultSetMapper;

    public UserDaoImpl(TransactionManager transactionManager, ResultSetMapper resultSetMapper) {
        this.transactionManager = transactionManager;
        this.resultSetMapper = resultSetMapper;
    }

    @Override
    public void create(User user) {
        transactionManager.execute(connection -> {
            try(PreparedStatement insertStatement = connection.prepareStatement(
                    "INSERT INTO user VALUES (?, ?, ?)"
            )){
                insertStatement.setString(1, user.getName());
                insertStatement.setString(2, user.getPassword());
                insertStatement.setInt(3, user.getRate());
                insertStatement.executeUpdate();
            } catch (SQLException e) {
                throw new DataAccessException(e);
            }
        });
    }

    @Override
    public void create(List<User> users) {
        transactionManager.execute(connection -> {

            try (PreparedStatement insertStatement = connection.prepareStatement(
                    "INSERT INTO user VALUES "
                    + valuesSequence(3, users.size()))
            ){
                for (int i = 0; i < users.size(); i++) {
                    User user = users.get(i);
                    insertStatement.setString((3 * i) + 1, user.getName());
                    insertStatement.setString((3 * i) + 2, user.getPassword());
                    insertStatement.setInt((3 * i) + 3, user.getRate());
                }
                insertStatement.executeUpdate();
            } catch (SQLException e) {
                throw new DataAccessException(e);
            }
        });
    }

    @Override
    public List<User> getUserSortedByRate(int offset, int limit){
        AtomicReference<List<User>> users = new AtomicReference<>();

        transactionManager.execute(connection -> {
            try(PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM user ORDER BY rate DESC limit ? offset ?"
            )){
                statement.setInt(1, limit);
                statement.setInt(2, offset);

                try(ResultSet resultSet = statement.executeQuery()) {
                    users.set(resultSetMapper.mapCollection(resultSet, User.class));
                }
            } catch (SQLException e) {
                throw new DataAccessException(e);
            }
        });

        return users.get();
    }

    @Override
    public int getUsersNumber(){
        AtomicInteger count = new AtomicInteger();

        transactionManager.execute(connection -> {
            try(
                    PreparedStatement statement = connection.prepareStatement(
                            "SELECT COUNT(*) FROM user"
                    );
                    ResultSet resultSet = statement.executeQuery();
            ){
                resultSet.next();
                count.set(resultSet.getInt(1));
            } catch (SQLException e) {
                throw new DataAccessException(e);
            }
        });

        return count.get();
    }

    @Override
    public void updateRate(Map<User, Integer> rateChangeByUser) {

        transactionManager.execute(connection -> {

            try (Statement statement = connection.createStatement()){

                var usersBySameRateChange = new LinkedHashMap<Integer, List<User>>();

                var selectQuery = new StringBuilder("SELECT rate FROM user WHERE name IN ");
                var names = new StringJoiner(", ", "( ", " )");

                rateChangeByUser.forEach((user, rate) -> {
                    usersBySameRateChange.computeIfAbsent(
                            rate,
                            r->new ArrayList<>()
                    ).add(user);

                    names.add("'"+user.getName()+"'");
                });

                selectQuery.append(names);
                selectQuery.append(" FOR SHARE;");

                statement.execute(selectQuery.toString());

                for (var entry : usersBySameRateChange.entrySet()){
                    int rate = entry.getKey();
                    var users = entry.getValue();

                    var updateQuery = new StringBuilder("UPDATE user SET rate = (rate + ")
                            .append(rate)
                            .append(") WHERE name IN ");

                    var values = new StringJoiner(", ", "( ", " );\n");
                    users.forEach(u->values.add("'"+u.getName()+"'"));
                    updateQuery.append(values);

                    statement.addBatch(updateQuery.toString());
                }

                statement.executeBatch();
            } catch (SQLException e) {
                throw new DataAccessException(e);
            }
        });
    }

    @Override
    public Optional<User> findByName(String name) {
        var user = new AtomicReference<Optional<User>>();

        transactionManager.execute(connection -> {
            try(PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM user WHERE name = ?"
            )){
                statement.setString(1, name);

                try (ResultSet resultSet = statement.executeQuery();){
                    user.set(resultSetMapper.map(resultSet, User.class));
                }
            } catch (SQLException e) {
                throw new DataAccessException(e);
            }
        });

        return user.get();
    }

    @Override
    public Collection<Role> getRolesByName(String name) {
        var roles = new AtomicReference<Collection<Role>>();

        transactionManager.execute(connection -> {
            try(PreparedStatement statement = connection.prepareStatement(
                             """
                             SELECT r.*, p.* FROM role r \
                             INNER JOIN user_role ur ON ur.user_name = ? \
                             INNER JOIN role_permission rp ON r.name = rp.role_name \
                             INNER JOIN  permission p ON rp.permission_id = p.id;
                             """
            )){
                statement.setString(1, name);

                try(ResultSet resultSet = statement.executeQuery()) {

                    var iterator = new ResultSetIterator(resultSet);
                    var roleByName = new HashMap<String, Role>();

                    while (iterator.hasNext()){
                        Map<String, Object> row = iterator.next();
                        Role mappedRole = resultSetMapper.map(row, Role.class);
                        Role role = roleByName.computeIfAbsent(mappedRole.getName(), roleName -> mappedRole);
                        Permission permission = resultSetMapper.map(row, Permission.class);
                        role.getPermissions().add(permission);
                    }

                    roles.set(roleByName.values());
                }
            } catch (SQLException e) {
                throw new DataAccessException(e);
            }
        });

        return roles.get();
    }

    @Override
    public List<User> findAll() {
        var users = new AtomicReference<List<User>>();

        transactionManager.execute(connection -> {
            try(
                    PreparedStatement statement = connection.prepareStatement("SELECT * FROM user");
                    ResultSet resultSet = statement.executeQuery()
            ) {
                users.set(resultSetMapper.mapCollection(resultSet, User.class));
            } catch (SQLException e) {
                throw new DataAccessException(e);
            }
        });

        return users.get();
    }

    @Override
    public void update(User value) {
        throw new RuntimeException("Method not implemented");
    }

    @Override
    public boolean deleteByName(String name) {
        throw new RuntimeException("Method not implemented");
    }

    @Override
    public void addUserRole(User user, RoleName roleName) {
        transactionManager.execute(connection -> {
            try(PreparedStatement insertStatement = connection.prepareStatement(
                    "INSERT INTO user_role VALUES (?, ?)"
            )){
                insertStatement.setString(1, user.getName());
                insertStatement.setString(2, roleName.toString());
                insertStatement.executeUpdate();
            } catch (SQLException e) {
                throw new DataAccessException(e);
            }
        });
    }

    private String valuesSequence(int valuesLength, int sequenceLength){
        var values = new StringJoiner(", ", "( ", " )");
        for (int j = 0; j < valuesLength; j++){
            values.add("?");
        }
        String element = values.toString();
        return element.repeat(sequenceLength);
    }

    private String valuesSequence(int valuesLength){
        return valuesSequence(valuesLength, 1);
    }
}
