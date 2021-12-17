package server.view.condition;

import domain.User;

public interface ViewCondition {
    boolean isSatisfied(User user);
}
