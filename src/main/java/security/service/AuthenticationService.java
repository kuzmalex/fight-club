package security.service;

import domain.User;
import security.UserDetails;
import server.JWebToken;

public interface AuthenticationService {
    JWebToken authenticate(User user, String password);
    UserDetails getUserDetails(User user);
}
