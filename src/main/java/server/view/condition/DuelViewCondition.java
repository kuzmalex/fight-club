package server.view.condition;

import domain.User;
import game.session.GameSession;
import game.service.GameSessionService;

import java.util.Optional;

public class DuelViewCondition implements ViewCondition {

    private final GameSessionService gameSessionService;

    public DuelViewCondition(GameSessionService gameSessionService) {
        this.gameSessionService = gameSessionService;
    }

    @Override
    public boolean isSatisfied(User user) {
        Optional<GameSession> gameSession = gameSessionService.getSessionByUser(user);
        return gameSessionService.isInGame(user) && gameSession.isPresent()
                && !gameSession.get().getGame().isFinished()
                && System.nanoTime() > gameSession.get().getStartTime();
    }
}
