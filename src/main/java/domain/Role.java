package domain;

import dao.mapper.Column;

import java.util.HashSet;
import java.util.Set;

public class Role {
    @Column(name = "name")
    private String name;
    private final Set<Permission> permissions;

    public Role(){
        permissions = new HashSet<>();
    }

    public Role(RoleName name, Set<Permission> permissions) {
        this.name = name.toString();
        this.permissions = permissions;
    }

    public String getName() {
        return name;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }
}
