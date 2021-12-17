package config.game.session;

import game.GameFactory;
import game.session.GameSessionFactory;
import game.session.GameSessionFactoryImpl;
import game.session.PlayerMatcher;
import infostructure.di.annotations.Configuration;
import infostructure.di.annotations.ManagedObject;
import infostructure.di.annotations.Value;
import game.service.GameResultService;
import game.service.GameSessionService;
import game.service.GameSessionServiceImpl;

import java.util.concurrent.ScheduledExecutorService;

@Configuration
public class GameSessionConfig {

    @ManagedObject
    public GameSessionFactory gameSessionFactory(
            GameFactory gameFactory,
            @Value("gameStartDelay") String startDelayValue
    ){
        int startDelay = Integer.parseInt(startDelayValue);
        return new GameSessionFactoryImpl(gameFactory, startDelay);
    }

    @ManagedObject
    public GameSessionService gameSessionService(
            PlayerMatcher playerMatcher,
            ScheduledExecutorService taskScheduler,
            GameSessionFactory gameSessionFactory,
            GameResultService gameResultService
    ){
        return new GameSessionServiceImpl(playerMatcher, taskScheduler, gameSessionFactory, gameResultService);
    }
}
