package config.view;

import dao.UserDao;
import infostructure.di.annotations.Configuration;
import infostructure.di.annotations.ManagedObject;
import infostructure.di.annotations.Named;
import infostructure.di.annotations.Value;
import server.RequestContext;
import server.view.factory.*;
import server.view.model.dto.RateTableModel;
import server.view.model.page.*;
import game.service.GameResultService;
import game.service.GameSessionService;

@Configuration
public class ViewModelFactoryConfig {

    @ManagedObject(name = "LoginModelFactory")
    public ViewModelFactory<LoginViewModel> loginModelFactory(@Value("login_request_path")String requestPath){
        return new LoginViewModelFactory(requestPath);
    }

    @ManagedObject(name = "StartModelFactory")
    public ViewModelFactory<StartViewModel> startModelFactory(
            @Named("RateTableModelFactory") ViewModelFactory<RateTableModel> rateTableFactory
    ){
        return new StartViewModelFactory(rateTableFactory);
    }

    @ManagedObject(name = "MatchWaitingModelFactory")
    public ViewModelFactory<MatchWaitingModel> matchWaitingModelFactory(){
        return new MatchWaitingViewModelFactory();
    }

    @ManagedObject(name = "TimerModelFactory")
    public ViewModelFactory<TimerViewModel> timerModelFactory(GameSessionService gameSessionService){
        return new TimerViewModelFactory(gameSessionService);
    }

    @ManagedObject(name = "DuelModelFactory")
    public ViewModelFactory<DuelViewModel> duelModelFactory(GameSessionService gameSessionService){
        return new DuelViewModelFactory(gameSessionService);
    }

    @ManagedObject(name = "GameResultModelFactory")
    public ViewModelFactory<GameResultViewModel> timerModelFactory(
            GameResultService gameResultService,
            @Named("StartModelFactory") ViewModelFactory<StartViewModel> startModelFactory
    ){
        return new GameResultViewModelFactory(startModelFactory, gameResultService);
    }

    @ManagedObject(name = "RateTableModelFactory")
    public ViewModelFactory<RateTableModel> startModelFactory(
            UserDao userDao,
            RequestContext requestContext,
            @Value("users_per_rate_table_page") String usersPerPageValue
    ){
        int usersPerPage = Integer.parseInt(usersPerPageValue);
        return new RateTableModelFactory(userDao, requestContext, usersPerPage);
    }
}
