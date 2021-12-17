package dao.cache;

import dao.UserDao;
import domain.Role;
import domain.RoleName;
import domain.User;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class UserCountCache implements UserDao {

    private final UserDao userDao;

    private final AtomicInteger count = new AtomicInteger(-1);

    public UserCountCache(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void create(User value) {
        userDao.create(value);
        if (count.get() == -1){
            count.compareAndExchange(-1, userDao.getUsersNumber());
        }
        else count.incrementAndGet();
    }

    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }

    @Override
    public void update(User value) {
        userDao.update(value);
    }

    @Override
    public void create(List<User> users) {
        userDao.create(users);
    }

    @Override
    public Optional<User> findByName(String name) {
        return userDao.findByName(name);
    }

    @Override
    public void updateRate(Map<User, Integer> rateChangeByUser) {
        userDao.updateRate(rateChangeByUser);
    }

    @Override
    public Collection<Role> getRolesByName(String name) {
        return userDao.getRolesByName(name);
    }

    @Override
    public List<User> getUserSortedByRate(int offset, int limit) {
        return userDao.getUserSortedByRate(offset, limit);
    }

    @Override
    public int getUsersNumber() {
        if (count.get() == -1){
            count.compareAndExchange(-1, userDao.getUsersNumber());
        }
        return count.get();
    }

    @Override
    public boolean deleteByName(String name) {
        boolean deleted = userDao.deleteByName(name);
        if (deleted) {
            count.decrementAndGet();
        }
        return deleted;
    }

    @Override
    public void addUserRole(User user, RoleName roleName) {
        userDao.addUserRole(user, roleName);
    }
}
