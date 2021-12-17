package dao;

import domain.Role;
import domain.RoleName;

import java.util.Optional;

public interface RoleDao extends Dao<Role>{

    Optional<Role> findByName(RoleName name);
}
