package dao;

import domain.Role;
import domain.RoleName;
import domain.User;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserDao extends Dao<User> {
    void create(List<User> users);
    Optional<User> findByName(String name);
    void updateRate(Map<User, Integer> rateChangeByUser);
    Collection<Role> getRolesByName(String name);
    List<User> getUserSortedByRate(int offset, int limit);
    int getUsersNumber();
    boolean deleteByName(String name);
    void addUserRole(User user, RoleName roleName);
}
