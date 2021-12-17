package config.view;

import game.service.GameResultService;
import infostructure.di.annotations.Configuration;
import infostructure.di.annotations.ManagedObject;
import server.RequestContext;
import server.view.condition.*;
import security.service.AuthorizationService;
import game.service.GameSessionService;

@Configuration
public class ViewConditionConfig {

    @ManagedObject(name = "LoginViewCondition")
    public ViewCondition loginViewCondition(
            AuthorizationService authorizationService,
            RequestContext context
    ){
        return new LoginViewCondition(context, authorizationService);
    }

    @ManagedObject(name = "StartViewCondition")
    public ViewCondition startViewCondition(
            GameSessionService gameSessionService,
            GameResultService gameResultService
    ){
        return new StartViewCondition(gameSessionService, gameResultService);
    }

    @ManagedObject(name = "MatchWaitingViewCondition")
    public ViewCondition waitingMatchViewCondition(GameSessionService gameSessionService){
        return new WaitingMatchViewCondition(gameSessionService);
    }

    @ManagedObject(name = "TimerViewCondition")
    public ViewCondition timerViewCondition(GameSessionService gameSessionService){
        return new TimerViewCondition(gameSessionService);
    }

    @ManagedObject(name = "DuelViewCondition")
    public ViewCondition duelViewCondition(GameSessionService gameSessionService){
        return new DuelViewCondition(gameSessionService);
    }

    @ManagedObject(name = "GameResultViewCondition")
    public ViewCondition gameResultViewCondition(
            GameResultService gameResultService,
            GameSessionService gameSessionService
    ){
        return new GameResultViewCondition(gameResultService, gameSessionService);
    }
}
