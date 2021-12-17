package config.game;

import dao.UserDao;
import game.DuelGameResultFactory;
import game.GameResultFactory;
import infostructure.di.annotations.Configuration;
import infostructure.di.annotations.ManagedObject;
import game.service.GameResultService;
import game.service.GameResultServiceImpl;

import java.util.concurrent.ScheduledExecutorService;

@Configuration
public class GameResultServiceConfig {

    @ManagedObject
    public GameResultService gameResultService(
            GameResultFactory gameResultFactory,
            UserDao userDao,
            ScheduledExecutorService taskScheduler
    ){
        return new GameResultServiceImpl(gameResultFactory, userDao, taskScheduler);
    }

    @ManagedObject
    public GameResultFactory gameResultFactory(){
        return new DuelGameResultFactory();
    }
}
