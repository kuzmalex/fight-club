package server.service;

import domain.User;

import java.util.Optional;

public interface UserService {
    Optional<User> register(String name, String password);
    Optional<User> getByName(String name);
}
