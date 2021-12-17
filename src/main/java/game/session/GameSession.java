package game.session;

import domain.User;
import game.Game;
import game.engine.unit.logger.EventsLogger;
import game.engine.unit.logger.EventsLoggerImpl;
import game.session.input.InputProcessor;

import java.util.Collection;

public class GameSession {

    private final static int LOG_LENGTH = 5;

    private final Game game;
    private final Collection<User> users;
    private final InputProcessor inputProcessor;
    private final long startTime;
    private final EventsLogger eventsLogger;

    public GameSession(
            Game game,
            Collection<User> users,
            InputProcessor inputProcessor,
            long startTime
    ) {
        this.game = game;
        this.users = users;
        this.inputProcessor = inputProcessor;
        this.startTime = startTime;
        eventsLogger = new EventsLoggerImpl(game, LOG_LENGTH);
    }

    public Game getGame() {
        return game;
    }

    public InputProcessor getInputProcessor() {
        return inputProcessor;
    }

    public long getStartTime() {
        return startTime;
    }

    public Collection<User> getUsers() {
        return users;
    }

    public EventsLogger getEventsLogger() {
        return eventsLogger;
    }
}
