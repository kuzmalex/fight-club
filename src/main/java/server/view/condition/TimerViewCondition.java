package server.view.condition;

import domain.User;
import game.service.GameSessionService;
import game.session.GameSession;

import java.util.Optional;

public class TimerViewCondition implements ViewCondition {

    private final GameSessionService gameSessionService;

    public TimerViewCondition(GameSessionService gameSessionService) {
        this.gameSessionService = gameSessionService;
    }

    @Override
    public boolean isSatisfied(User user) {
        Optional<GameSession> gameSession = gameSessionService.getSessionByUser(user);
        return gameSession.isPresent() &&
                System.nanoTime() < gameSession.get().getStartTime();
    }
}
