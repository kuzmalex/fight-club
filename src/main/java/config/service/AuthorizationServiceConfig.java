package config.service;

import dao.RoleDao;
import dao.impl.PermissionDao;
import domain.RoleName;
import infostructure.di.annotations.Configuration;
import infostructure.di.annotations.ManagedObject;
import security.service.AuthorizationService;
import security.service.AuthorizationServiceImpl;

@Configuration
public class AuthorizationServiceConfig {

    @ManagedObject
    public AuthorizationService authorizationService(PermissionDao permissionDao, RoleDao roleDao){
        return new AuthorizationServiceImpl(permissionDao, roleDao, RoleName.guest);
    }
}
