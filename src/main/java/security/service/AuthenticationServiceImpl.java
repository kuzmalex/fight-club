package security.service;

import dao.UserDao;
import domain.Role;
import domain.User;
import security.UserDetails;
import server.JWebToken;
import server.PasswordCrypt;
import server.handlers.exception.InvalidPasswordException;

import java.util.ArrayList;
import java.util.List;

public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserDao userDao;
    private final PasswordCrypt passwordCrypt = new PasswordCrypt();

    public AuthenticationServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    public JWebToken authenticate(User user, String password){
        boolean valid = passwordCrypt.authenticate(password.toCharArray(), user.getPassword());
        if (!valid) {
            throw new InvalidPasswordException();
        }

        UserDetails userDetails = getUserDetails(user);
        return new JWebToken(userDetails);
    }

    @Override
    public UserDetails getUserDetails(User user) {
        List<Role> roles = new ArrayList<>(userDao.getRolesByName(user.getName()));
        return new UserDetails(user.getName(), roles, Long.MAX_VALUE);
    }
}
