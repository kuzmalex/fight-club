package game.session;

import java.util.List;

public class Match {
    private final List<MatchedTeam> matchedTeams;

    public Match(List<MatchedTeam> matchedTeams) {
        this.matchedTeams = matchedTeams;
    }

    public List<MatchedTeam> getMatchedTeams() {
        return matchedTeams;
    }
}
