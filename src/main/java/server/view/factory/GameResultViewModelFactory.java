package server.view.factory;

import domain.User;
import game.GameResult;
import server.view.model.page.GameResultViewModel;
import server.view.model.page.StartViewModel;
import game.service.GameResultService;

public class GameResultViewModelFactory implements ViewModelFactory<GameResultViewModel>{

    private final ViewModelFactory<StartViewModel> startModelFactory;
    private final GameResultService gameResultService;

    public GameResultViewModelFactory(ViewModelFactory<StartViewModel> startModelFactory, GameResultService gameResultService) {
        this.startModelFactory = startModelFactory;
        this.gameResultService = gameResultService;
    }

    @Override
    public GameResultViewModel create(User user) {
        StartViewModel startModel = startModelFactory.create(user);

        GameResult gameResult = gameResultService.getLastResult(user).orElseThrow();
        String lastGameResult = toRecord(gameResult, user);

        return new GameResultViewModel(startModel, lastGameResult);
    }

    private String toRecord(GameResult gameResult, User user){
        return gameResult.winners()
                .stream()
                .anyMatch(player->
                        player.getName().equals(user.getName())
                )
                ? "Вы победили!"
                : "Вы проиграли";
    }
}
