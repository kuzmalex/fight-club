package domain;

import dao.mapper.Column;

import java.util.Objects;

public class User {

    public final static String GUEST_USER_NAME = "anonymous";

    @Column(name = "name")
    private String name;
    @Column(name = "password")
    private String password;
    @Column(name = "rate")
    private int rate;

    public User(){}

    public User(String name, String password, int rate) {
        this.name = name;
        this.password = password;
        this.rate = rate;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public int getRate() {
        return rate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(name, user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
