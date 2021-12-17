package config.service;

import dao.UserDao;
import infostructure.di.annotations.Configuration;
import infostructure.di.annotations.ManagedObject;
import security.service.AuthenticationService;
import security.service.AuthenticationServiceImpl;

@Configuration
public class AuthenticationServiceConfig {

    @ManagedObject
    public AuthenticationService authenticationService(UserDao userDao){
        return new AuthenticationServiceImpl(userDao);
    }
}
