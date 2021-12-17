package dao.impl;

import dao.PlayerStatsDao;
import dao.exception.DataAccessException;
import dao.mapper.ResultSetMapper;
import dao.transaction.TransactionManager;
import domain.PlayerStats;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class PlayerStatsDaoImpl implements PlayerStatsDao {

    private final TransactionManager transactionManager;
    private final ResultSetMapper resultSetMapper;

    public PlayerStatsDaoImpl(TransactionManager transactionManager, ResultSetMapper resultSetMapper) {
        this.transactionManager = transactionManager;
        this.resultSetMapper = resultSetMapper;
    }

    @Override
    public void create(PlayerStats stats) {
        transactionManager.execute(connection -> {
            try(PreparedStatement insertStatement = connection.prepareStatement(
                    "INSERT INTO player_stats VALUES (?, ?, ?)"
            )){
                insertStatement.setString(1, stats.getUserName());
                insertStatement.setDouble(2, stats.getHealth());
                insertStatement.setDouble(3, stats.getStrength());
                insertStatement.executeUpdate();
            } catch (SQLException e) {
                throw new DataAccessException(e);
            }
        });
    }

    @Override
    public List<PlayerStats> findAll() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void update(PlayerStats value) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public Optional<PlayerStats> findByUserName(String name) {
        var stats = new AtomicReference<Optional<PlayerStats>>();

        transactionManager.execute(connection -> {
            try(PreparedStatement insertStatement = connection.prepareStatement(
                    "SELECT * FROM player_stats WHERE name = ?"
            )){
                insertStatement.setString(1, name);
                ResultSet resultSet = insertStatement.executeQuery();

                stats.set(resultSetMapper.map(resultSet, PlayerStats.class));
            } catch (SQLException e) {
                throw new DataAccessException(e);
            }
        });

        return stats.get();
    }
}
