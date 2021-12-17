package security.service;

import domain.Role;
import security.UserDetails;

public interface AuthorizationService {
    boolean isPermitted(UserDetails userDetails, String path);
    Role getGuestRole();
    boolean isGuest(UserDetails userDetails);
}
