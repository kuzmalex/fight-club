package server.service;

import dao.PlayerStatsDao;
import dao.UserDao;
import dao.transaction.TransactionManager;
import domain.PlayerStats;
import domain.RoleName;
import domain.User;
import server.PasswordCrypt;
import server.RequestContext;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

public class UserServiceImpl implements UserService {

    private static final int DEFAULT_STRENGTH = 10;
    private static final int DEFAULT_HEALTH = 100;

    private final String NAME_REGEX = "^[]A-Za-z]\\w{2,29}$";
    private final Pattern NAME_PATTERN = Pattern.compile(NAME_REGEX);

    private final UserDao userDao;
    private final PlayerStatsDao playerStatsDao;
    private final PasswordCrypt passwordCrypt;
    private final TransactionManager transactionManager;
    private final RequestContext requestContext;

    public UserServiceImpl(
            UserDao userDao,
            PlayerStatsDao playerStatsDao,
            PasswordCrypt passwordCrypt,
            TransactionManager transactionManager,
            RequestContext requestContext
    ) {
        this.userDao = userDao;
        this.playerStatsDao = playerStatsDao;
        this.passwordCrypt = passwordCrypt;
        this.transactionManager = transactionManager;
        this.requestContext = requestContext;
    }

    public Optional<User> register(String name, String password){
        Objects.requireNonNull(name);
        Objects.requireNonNull(password);

        if (!validateName(name)){
            return Optional.empty();
        }

        String hashedPassword = passwordCrypt.hash(password.toCharArray());

        transactionManager.begin();

        User user = new User(name, hashedPassword, 0);
        userDao.create(user);

        userDao.addUserRole(user, RoleName.player);

        PlayerStats playerStats = new PlayerStats(user.getName(), DEFAULT_HEALTH, DEFAULT_STRENGTH);
        playerStatsDao.create(playerStats);

        transactionManager.commit();

        return Optional.of(user);
    }

    private boolean validateName(String name) {
        return NAME_PATTERN.matcher(name).matches();
    }

    public Optional<User> getByName(String name){
        if (name.equals(User.GUEST_USER_NAME)){
            User guest = new User(name, "", -1);
            return Optional.of(guest);
        }

        User cashedUser = requestContext.getUser();
        if (cashedUser != null){
            return Optional.of(cashedUser);
        }

        Optional<User> user = userDao.findByName(name);
        user.ifPresent(requestContext::setUser);

        return user;
    }
}
