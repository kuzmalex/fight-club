package config.service;

import infostructure.di.annotations.Configuration;
import infostructure.di.annotations.ManagedObject;
import server.PasswordCrypt;

@Configuration
public class CryptConfig {

    @ManagedObject
    public PasswordCrypt passwordCrypt(){
        return new PasswordCrypt();
    }
}
