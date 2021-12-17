package game.session;

import domain.User;

import java.util.List;

public class MatchedTeam {
    private final List<User> users;

    public MatchedTeam(User user) {
        this.users = List.of(user);
    }

    public MatchedTeam(List<User> users) {
        this.users = users;
    }

    public List<User> getUsers() {
        return users;
    }
}
