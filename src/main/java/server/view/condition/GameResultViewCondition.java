package server.view.condition;

import domain.User;
import game.GameResult;
import game.service.GameResultService;
import game.service.GameSessionService;

import java.util.Optional;

public class GameResultViewCondition implements ViewCondition{

    private final GameResultService gameResultService;
    private final GameSessionService gameSessionService;

    public GameResultViewCondition(
            GameResultService gameResultService,
            GameSessionService gameSessionService
    ) {
        this.gameResultService = gameResultService;
        this.gameSessionService = gameSessionService;
    }

    @Override
    public boolean isSatisfied(User user) {
        Optional<GameResult> gameResult = gameResultService.getLastResult(user);
        return gameResult.isPresent() &&
                !(gameSessionService.isWaitingToBeMatched(user) || gameSessionService.isInGame(user));
    }
}
