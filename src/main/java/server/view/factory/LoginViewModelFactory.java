package server.view.factory;

import domain.User;
import server.view.model.page.LoginViewModel;

public class LoginViewModelFactory implements ViewModelFactory<LoginViewModel> {

    private final String loginRequestPath;

    public LoginViewModelFactory(String loginRequestPath) {
        this.loginRequestPath = loginRequestPath;
    }

    public LoginViewModel create(User unused){
        return new LoginViewModel(loginRequestPath);
    }
}
