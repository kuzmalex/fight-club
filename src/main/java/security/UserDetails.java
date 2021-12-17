package security;

import domain.Role;

import java.util.List;

public class UserDetails {

    private final String subject;
    private final List<Role> roles;
    private final long expires;

    public UserDetails(String subject, List<Role> roles, long expires) {
        this.subject = subject;
        this.roles = roles;
        this.expires = expires;
    }

    public UserDetails(String subject, Role role, long expires) {
        this.subject = subject;
        this.roles = List.of(role);
        this.expires = expires;
    }

    public String getSubject() {
        return subject;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public long getExpires() {
        return expires;
    }
}
