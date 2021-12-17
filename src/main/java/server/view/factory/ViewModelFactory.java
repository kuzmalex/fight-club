package server.view.factory;

import domain.User;
import server.view.Model;

public interface ViewModelFactory<M extends Model> {
    M create(User user);
}
