package game.service;

import domain.User;
import game.session.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameSessionServiceImpl implements GameSessionService {

    private final long RESULT_EXPIRATION_TIME = 3600;

    private final PlayerMatcher playerMatcher;
    private final GameSessionFactory gameSessionFactory;
    private final GameResultService gameResultService;

    private final Map<User, GameSession> gameSessionByUser = new ConcurrentHashMap<>();

    public GameSessionServiceImpl(
            PlayerMatcher playerMatcher,
            ScheduledExecutorService taskScheduler,
            GameSessionFactory gameSessionFactory,
            GameResultService gameResultService
    ){
        this.playerMatcher = playerMatcher;
        this.gameSessionFactory = gameSessionFactory;
        this.gameResultService = gameResultService;

        taskScheduler.scheduleAtFixedRate(this::processMatches, 0, 100, TimeUnit.MILLISECONDS);
        taskScheduler.scheduleAtFixedRate(this::removeOldSessions, 0, 1, TimeUnit.SECONDS);
    }

    public void joinGame(User user){
        if (!gameSessionByUser.containsKey(user)){
            playerMatcher.addPlayerToMatch(user);
            processMatches();
        }
    }

    @Override
    public boolean isWaitingToBeMatched(User user) {
        return playerMatcher.isQueued(user);
    }

    @Override
    public boolean isInGame(User user) {
        return gameSessionByUser.containsKey(user);
    }

    @Override
    public Optional<GameSession> getSessionByUser(User user) {
        return Optional.ofNullable(gameSessionByUser.get(user));
    }

    private void processMatches(){
        Optional<Match> match = playerMatcher.getNextMatch();
        while (match.isPresent()){
            createGameSession(match.get());
            match = playerMatcher.getNextMatch();
        }
    }

    private void createGameSession(Match match){
        GameSession gameSession = gameSessionFactory.create(match);

        var users = new ArrayList<User>();
        for (MatchedTeam team : match.getMatchedTeams()){
            for (User user : team.getUsers()){
                gameSessionByUser.put(user, gameSession);
                users.add(user);
            }
        }

        gameSession.getGame().addListener(
                event -> {
                    gameResultService.handleResult(gameSession);
                    users.forEach(gameSessionByUser::remove);
                }
        );
    }

    private void removeOldSessions(){
        gameSessionByUser.forEach(
                (user, session) -> {
                    long sessionLifeTime = TimeUnit.NANOSECONDS.toSeconds(
                            System.nanoTime() - session.getStartTime()
                    );
                    if (sessionLifeTime > RESULT_EXPIRATION_TIME){
                        GameSession removedSession = gameSessionByUser.remove(user);
                        if (removedSession != session){
                            gameSessionByUser.put(user, removedSession);
                        }
                    }
                }
        );
    }
}
