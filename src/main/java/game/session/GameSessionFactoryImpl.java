package game.session;

import domain.User;
import game.Game;
import game.GameFactory;
import game.session.input.InputProcessor;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class GameSessionFactoryImpl implements GameSessionFactory {

    private final GameFactory gameFactory;
    private final long startDelay;

    public GameSessionFactoryImpl(GameFactory gameFactory, long startDelay) {
        this.gameFactory = gameFactory;
        this.startDelay = startDelay;
    }

    @Override
    public GameSession create(Match match) {

        Game game = gameFactory.create(match.getMatchedTeams());

        InputProcessor inputProcessor = new InputProcessor(game.getControllers());

        long startTime = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(startDelay);

        Set<User> users = match.getMatchedTeams().stream()
                .flatMap(team -> team.getUsers().stream())
                .collect(Collectors.toSet());

        return new GameSession(game, users, inputProcessor, startTime);
    }
}
