package security.service;

import dao.RoleDao;
import dao.impl.PermissionDao;
import domain.Permission;
import domain.Role;
import domain.RoleName;
import infostructure.di.annotations.AfterContextInitialization;
import security.UserDetails;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AuthorizationServiceImpl implements AuthorizationService {

    private final PermissionDao permissionDao;
    private final RoleDao roleDao;
    private RoleName guestRoleName;
    private Role guestRole;
    private Map<String, Permission> permissionsByResource;

    public AuthorizationServiceImpl(PermissionDao permissionDao, RoleDao roleDao, RoleName guestRoleName) {
        this.roleDao = roleDao;
        this.permissionDao = permissionDao;
        this.guestRoleName = guestRoleName;
    }

    @AfterContextInitialization
    private void init(){
        guestRole = roleDao.findByName(guestRoleName).orElseThrow();
        permissionsByResource = permissionDao.findAll()
                .stream()
                .collect(Collectors.toMap(Permission::getResource, p->p));
    }

    @Override
    public boolean isPermitted(UserDetails userDetails, String path) {
        for (Role role : userDetails.getRoles()){
            if (role.getPermissions().contains(permissionsByResource.get(path))){
                return true;
            }
        }
        return false;
    }

    @Override
    public Role getGuestRole() {
        return guestRole;
    }

    @Override
    public boolean isGuest(UserDetails userDetails) {
        List<Role> roles = userDetails.getRoles();

        return roles.size() == 1 && roles.get(0).equals(guestRole);
    }

}
