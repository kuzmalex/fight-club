package server.view.condition;

import domain.User;
import game.service.GameSessionService;

public class WaitingMatchViewCondition implements ViewCondition{

    private final GameSessionService gameSessionService;

    public WaitingMatchViewCondition(GameSessionService gameSessionService) {
        this.gameSessionService = gameSessionService;
    }

    @Override
    public boolean isSatisfied(User user) {
        return gameSessionService.isWaitingToBeMatched(user);
    }
}
