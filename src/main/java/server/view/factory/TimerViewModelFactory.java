package server.view.factory;

import domain.User;
import game.session.GameSession;
import server.view.model.page.TimerViewModel;
import game.service.GameSessionService;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class TimerViewModelFactory implements ViewModelFactory<TimerViewModel>{

    private final GameSessionService sessionService;

    public TimerViewModelFactory(GameSessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public TimerViewModel create(User user) {
        Optional<GameSession> gameSession = sessionService.getSessionByUser(user);
        long timeBeforeStart = gameSession.orElseThrow().getStartTime() - System.nanoTime();
        int secondsBeforeStart = (int)(TimeUnit.NANOSECONDS.toSeconds(timeBeforeStart));
        return new TimerViewModel(secondsBeforeStart);
    }
}
