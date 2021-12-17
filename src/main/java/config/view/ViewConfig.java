package config.view;

import infostructure.Properties;
import infostructure.di.annotations.Configuration;
import infostructure.di.annotations.ManagedObject;
import server.view.BaseView;
import server.view.View;
import server.view.html.Component;
import server.view.html.PageFactory;
import server.view.model.page.*;
import server.service.ComponentService;

@Configuration
public class ViewConfig {

    @ManagedObject(name = "LoginView")
    public View<LoginViewModel> loginView(
            Properties properties,
            ComponentService componentService,
            PageFactory pageFactory
    ){
        Component component = componentService.getComponent(properties.getValue("login_page_component"));
        return new BaseView<>(component, componentService, pageFactory);
    }

    @ManagedObject(name = "StartView")
    public View<StartViewModel> startView(
            Properties properties,
            ComponentService componentService,
            PageFactory pageFactory
    ){
        Component component = componentService.getComponent(properties.getValue("start_page_component"));
        return new BaseView<>(component, componentService, pageFactory);
    }

    @ManagedObject(name = "MatchWaitingView")
    public View<MatchWaitingModel> matchWaitingView(
            Properties properties,
            ComponentService componentService,
            PageFactory pageFactory
    ){
        Component component = componentService.getComponent(properties.getValue("matching_page_component"));
        return new BaseView<>(component, componentService, pageFactory);
    }

    @ManagedObject(name = "TimerView")
    public View<TimerViewModel> startTimerView(
            Properties properties,
            ComponentService componentService,
            PageFactory pageFactory
    ){
        Component component = componentService.getComponent(properties.getValue("timer_page_component"));
        return new BaseView<>(component, componentService, pageFactory);
    }

    @ManagedObject(name = "DuelView")
    public View<DuelViewModel> duelView(
            Properties properties,
            ComponentService componentService,
            PageFactory pageFactory
    ){
        Component component = componentService.getComponent(properties.getValue("duel_page_component"));
        return new BaseView<>(component, componentService, pageFactory);
    }

    @ManagedObject(name = "GameResultView")
    public View<GameResultViewModel> gameResultView(
            Properties properties,
            ComponentService componentService,
            PageFactory pageFactory
    ){
        Component component = componentService.getComponent(properties.getValue("game_result_page_component"));
        return new BaseView<>(component, componentService, pageFactory);
    }
}
