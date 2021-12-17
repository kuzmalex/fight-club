package server.view.factory;

import domain.User;
import server.view.model.page.MatchWaitingModel;

public class MatchWaitingViewModelFactory implements ViewModelFactory<MatchWaitingModel> {

    @Override
    public MatchWaitingModel create(User unused) {
        return new MatchWaitingModel();
    }
}
