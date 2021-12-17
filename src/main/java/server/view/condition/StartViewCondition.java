package server.view.condition;

import domain.User;
import game.service.GameResultService;
import game.service.GameSessionService;

public class StartViewCondition implements ViewCondition{

    private final GameSessionService gameSessionService;
    private final GameResultService gameResultService;

    public StartViewCondition(GameSessionService gameSessionService, GameResultService gameResultService) {
        this.gameSessionService = gameSessionService;
        this.gameResultService = gameResultService;
    }

    @Override
    public boolean isSatisfied(User user) {
        return !(gameSessionService.isWaitingToBeMatched(user) ||
                        gameSessionService.isInGame(user) || gameResultService.getLastResult(user).isPresent()
        );
    }
}
