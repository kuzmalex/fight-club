package game.session;

import domain.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

public class DuelPlayerMatcher implements PlayerMatcher {

    private final ConcurrentLinkedDeque<User> queuedUsers = new ConcurrentLinkedDeque<>();
    private final Set<User> queuedUserSet = ConcurrentHashMap.newKeySet();

    public void addPlayerToMatch(User user){
        if (!queuedUserSet.contains(user)) {
            queuedUserSet.add(user);
            queuedUsers.add(user);
        }
    }

    @Override
    public boolean isQueued(User user) {
        return queuedUserSet.contains(user);
    }

    public Optional<Match> getNextMatch(){

        User firstUser = queuedUsers.poll();
        User secondUser = queuedUsers.poll();

        if (firstUser != null && secondUser != null){
            MatchedTeam firstTeam = new MatchedTeam(firstUser);
            MatchedTeam secondTeam = new MatchedTeam(secondUser);

            queuedUserSet.remove(firstUser);
            queuedUserSet.remove(secondUser);

            return Optional.of(
                    new Match(List.of(firstTeam, secondTeam))
            );
        }

        if (firstUser != null){
            queuedUsers.addFirst(firstUser);
        }
        if (secondUser != null){
            queuedUsers.addFirst(secondUser);
        }

        return Optional.empty();
    }
}
