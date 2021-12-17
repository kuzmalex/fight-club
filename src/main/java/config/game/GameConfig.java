package config.game;

import dao.PlayerStatsDao;
import game.DuelGameFactory;
import game.GameFactory;
import infostructure.di.annotations.Configuration;
import infostructure.di.annotations.ManagedObject;

@Configuration
public class GameConfig {

    @ManagedObject
    public GameFactory gameFactory(PlayerStatsDao playerStatsDao){
        return new DuelGameFactory(playerStatsDao);
    }
}
