package game.service;

import domain.User;
import game.GameResult;
import game.session.GameSession;

import java.util.Optional;

public interface GameResultService {
    void handleResult(GameSession session);
    Optional<GameResult> getLastResult(User user);
}
