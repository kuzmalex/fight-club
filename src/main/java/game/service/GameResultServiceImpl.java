package game.service;

import dao.UserDao;
import domain.User;
import game.GameResult;
import game.GameResultFactory;
import game.engine.unit.Player;
import game.session.GameSession;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class GameResultServiceImpl implements GameResultService {

    private final long RESULT_EXPIRATION_TIME = 3600;

    private final GameResultFactory gameResultFactory;
    private final UserDao userDao;
    private final ScheduledExecutorService taskScheduler;
    private final Map<User, GameResult> lastResultsByUser = new ConcurrentHashMap<>();

    public GameResultServiceImpl(
            GameResultFactory gameResultFactory,
            UserDao userDao,
            ScheduledExecutorService taskScheduler
    ) {
        this.gameResultFactory = gameResultFactory;
        this.userDao = userDao;
        this.taskScheduler = taskScheduler;
    }

    @Override
    public void handleResult(GameSession session) {
        GameResult result = gameResultFactory.create(session.getGame());

        Set<String> winnerNames = result.winners().stream()
                .map(Player::getName)
                .collect(Collectors.toSet());

        Map<User, Integer> rateChangeByUser = new HashMap<>();
        session.getUsers().forEach(u->{
            lastResultsByUser.put(u, result);
            int rateChange = winnerNames.contains(u.getName()) ? 1 : -1;
            rateChangeByUser.put(u, rateChange);
        });
        userDao.updateRate(rateChangeByUser);
    }

    @Override
    public Optional<GameResult> getLastResult(User user) {
        return Optional.ofNullable(lastResultsByUser.get(user));
    }

    private void removeOldResults(){
        lastResultsByUser.forEach(
                (user, result) -> {
                    long sessionLifeTime = TimeUnit.NANOSECONDS.toSeconds(
                            System.nanoTime() - result.time()
                    );
                    if (sessionLifeTime > RESULT_EXPIRATION_TIME){
                        GameResult removedResult = lastResultsByUser.remove(user);
                        if (removedResult != result){
                            lastResultsByUser.put(user, removedResult);
                        }
                    }
                }
        );
    }
}
