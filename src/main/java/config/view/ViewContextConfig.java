package config.view;

import infostructure.di.annotations.Configuration;
import infostructure.di.annotations.ManagedCollectionElement;
import infostructure.di.annotations.Named;
import server.view.View;
import server.view.ViewContext;
import server.view.condition.ViewCondition;
import server.view.factory.ViewModelFactory;
import server.view.model.page.*;

@Configuration
public class ViewContextConfig {

    @ManagedCollectionElement(name = "contexts", order = 1)
    public ViewContext<LoginViewModel> loginViewContext(
            @Named("LoginViewCondition")ViewCondition condition,
            @Named("LoginView")View<LoginViewModel> view,
            @Named("LoginModelFactory") ViewModelFactory<LoginViewModel> modelFactory
    ){
        return new ViewContext<>(condition, view, modelFactory);
    }

    @ManagedCollectionElement(name = "contexts")
    public ViewContext<StartViewModel> startViewContext(
            @Named("StartViewCondition") ViewCondition condition,
            @Named("StartView")View<StartViewModel> view,
            @Named("StartModelFactory") ViewModelFactory<StartViewModel> modelFactory
    ){
        return new ViewContext<>(condition, view, modelFactory);
    }

    @ManagedCollectionElement(name = "contexts")
    public ViewContext<MatchWaitingModel> matchWaitingViewContext(
            @Named("MatchWaitingViewCondition") ViewCondition condition,
            @Named("MatchWaitingView")View<MatchWaitingModel> view,
            @Named("MatchWaitingModelFactory") ViewModelFactory<MatchWaitingModel> modelFactory
    ){
        return new ViewContext<>(condition, view, modelFactory);
    }

    @ManagedCollectionElement(name = "contexts")
    public ViewContext<TimerViewModel> timerViewContext(
            @Named("TimerViewCondition") ViewCondition condition,
            @Named("TimerView")View<TimerViewModel> view,
            @Named("TimerModelFactory") ViewModelFactory<TimerViewModel> modelFactory
    ){
        return new ViewContext<>(condition, view, modelFactory);
    }

    @ManagedCollectionElement(name = "contexts")
    public ViewContext<DuelViewModel> duelViewContext(
            @Named("DuelViewCondition") ViewCondition condition,
            @Named("DuelView")View<DuelViewModel> view,
            @Named("DuelModelFactory") ViewModelFactory<DuelViewModel> modelFactory
    ){
        return new ViewContext<>(condition, view, modelFactory);
    }

    @ManagedCollectionElement(name = "contexts")
    public ViewContext<GameResultViewModel> gameResultViewContext(
            @Named("GameResultViewCondition") ViewCondition condition,
            @Named("GameResultView")View<GameResultViewModel> view,
            @Named("GameResultModelFactory") ViewModelFactory<GameResultViewModel> modelFactory
    ){
        return new ViewContext<>(condition, view, modelFactory);
    }
}
