package server.view;

import domain.User;

public interface ViewResolver {
    String getPage(User user);
}
