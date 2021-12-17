package config.service;

import dao.PlayerStatsDao;
import dao.UserDao;
import dao.transaction.TransactionManager;
import infostructure.di.annotations.Configuration;
import infostructure.di.annotations.ManagedObject;
import server.PasswordCrypt;
import server.RequestContext;
import server.service.UserService;
import server.service.UserServiceImpl;

@Configuration
public class UserServiceConfig {

    @ManagedObject
    public UserService userService(
            UserDao userDao,
            PlayerStatsDao playerStatsDao,
            PasswordCrypt passwordCrypt,
            TransactionManager transactionManager,
            RequestContext requestContext
    ){
        return new UserServiceImpl(
                userDao,
                playerStatsDao,
                passwordCrypt,
                transactionManager,
                requestContext
        );
    }
}
