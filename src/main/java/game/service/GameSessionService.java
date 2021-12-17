package game.service;

import domain.User;
import game.session.GameSession;

import java.util.Optional;

public interface GameSessionService {
    void joinGame(User user);
    boolean isWaitingToBeMatched(User user);
    boolean isInGame(User user);
    Optional<GameSession> getSessionByUser(User user);
}
