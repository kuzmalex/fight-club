package dao;

import domain.PlayerStats;

import java.util.Optional;

public interface PlayerStatsDao extends Dao<PlayerStats>{
    Optional<PlayerStats> findByUserName(String name);
}
