package server.view.factory;

import domain.User;
import server.view.model.dto.RateTableModel;
import server.view.model.page.StartViewModel;

public class StartViewModelFactory implements ViewModelFactory<StartViewModel>{

    private final ViewModelFactory<RateTableModel> rateTableModelFactory;

    public StartViewModelFactory(ViewModelFactory<RateTableModel> rateTableModelFactory) {
        this.rateTableModelFactory = rateTableModelFactory;
    }

    @Override
    public StartViewModel create(User user) {
        return new StartViewModel(user.getRate(), rateTableModelFactory.create(user));
    }
}
