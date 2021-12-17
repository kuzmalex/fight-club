package dao.cache;

import dao.UserDao;
import domain.Role;
import domain.RoleName;
import domain.User;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SortedUserCache implements UserDao {

    private final UserDao userDao;
    private final ScheduledExecutorService taskScheduler;

    private final Comparator<User> rateComparator = Comparator.comparingDouble(User::getRate);

    private final int sortedUsersCacheSize = 50;

    private final Map<String, User> cashedUsersByName = new ConcurrentHashMap<>(sortedUsersCacheSize);
    private List<User> cashedSortedByRateUsers;
    private int minCachedRate;

    private volatile boolean cacheSpoiled = false;
    private final ReadWriteLock cacheLock = new ReentrantReadWriteLock();

    private final Object initLock = new Object();
    private volatile boolean initialized = false;

    public SortedUserCache(UserDao userDao, ScheduledExecutorService taskScheduler) {
        this.userDao = userDao;
        this.taskScheduler = taskScheduler;
    }

    private void init(){
        synchronized (initLock){
            if (!initialized){
                refreshCache();

                //to avoid synchronization with update/insert operations while refreshing
                taskScheduler.scheduleAtFixedRate(this::refreshCache, 0, 1000, TimeUnit.MILLISECONDS);

                initialized = true;
            }
        }

    }

    private void refreshCache() {
        Lock writeLock = cacheLock.writeLock();
        writeLock.lock();

        cashedSortedByRateUsers = userDao.getUserSortedByRate(0, sortedUsersCacheSize);

        cashedUsersByName.clear();
        cashedSortedByRateUsers.forEach(u -> cashedUsersByName.put(u.getName(), u));

        minCachedRate = cashedSortedByRateUsers.get(cashedSortedByRateUsers.size()-1).getRate();

        cacheSpoiled = false;

        writeLock.unlock();
    }

    @Override
    public void create(User value) {
        userDao.create(value);
        validateCache(value);
    }

    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }

    private void validateCache(User changedUser){
        if (!initialized || cacheSpoiled) return;

        User cachedUser = cashedUsersByName.get(changedUser.getName());
        if (changedUser.getRate() > minCachedRate){
            boolean spoiled = cachedUser == null;
            if (spoiled) cacheSpoiled = true;

            if (!spoiled){
                Lock writeLock = cacheLock.writeLock();
                writeLock.lock();
                cashedSortedByRateUsers.sort(rateComparator);
                writeLock.unlock();
            }
        }
        if (changedUser.getRate() <= minCachedRate && cachedUser != null){
            cacheSpoiled = true;
        }
    }

    @Override
    public void update(User value) {
        userDao.update(value);
        validateCache(value);
    }

    @Override
    public boolean deleteByName(String name) {

        boolean deleted = userDao.deleteByName(name);

        if (!cacheSpoiled && initialized){
            User cachedUser = cashedUsersByName.get(name);
            if (deleted && cachedUser != null) {
                cacheSpoiled = true;
            }
        }

        return deleted;
    }

    @Override
    public void addUserRole(User user, RoleName roleName) {
        userDao.addUserRole(user, roleName);
    }

    @Override
    public void create(List<User> users) {
        userDao.create(users);
        users.forEach(this::validateCache);
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
        if (!initialized) init();

        if (offset+limit <= sortedUsersCacheSize){
            if (cacheSpoiled){
                refreshCache();
            }
            return getCashedSortedByRateUsers(offset, limit);
        }
        return userDao.getUserSortedByRate(offset, limit);
    }

    private List<User> getCashedSortedByRateUsers(int offset, int limit){
        Lock readLock = cacheLock.readLock();
        readLock.lock();
        List<User> users = cashedSortedByRateUsers.subList(offset, Math.min(offset+limit, cashedSortedByRateUsers.size()));
        readLock.unlock();
        return users;
    }

    @Override
    public int getUsersNumber() {
        return userDao.getUsersNumber();
    }
}
