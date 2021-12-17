package game.session;

import domain.User;

import java.util.Optional;

public interface PlayerMatcher {
    void addPlayerToMatch(User user);
    boolean isQueued(User user);
    Optional<Match> getNextMatch();
}
