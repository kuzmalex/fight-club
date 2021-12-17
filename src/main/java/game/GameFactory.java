package game;

import game.session.MatchedTeam;

import java.util.List;

public interface GameFactory {
    Game create(List<MatchedTeam> teams);
}
