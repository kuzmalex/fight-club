package domain;

import dao.mapper.Column;

import java.util.Objects;

public class Permission {
    @Column(name = "name")
    private String name;
    @Column(name = "resource")
    private String resource;

    public String getName() {
        return name;
    }

    public String getResource() {
        return resource;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Permission that = (Permission) o;
        return Objects.equals(name, that.name) && Objects.equals(resource, that.resource);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, resource);
    }
}
