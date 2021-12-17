package game.session.input;

import domain.User;
import game.engine.command.Controller;
import game.engine.command.HitCommand;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InputProcessor implements Input {
    Map<String, Controller> controllerByUserName;

    public InputProcessor(List<Controller> controllers){

        controllerByUserName = new HashMap<>();

        for (Controller controller : controllers){
            controllerByUserName.put(controller.getPlayer().getName(), controller);
        }
    }

    public void hitBy(User user){
        String userName = user.getName();
        controllerByUserName.get(userName).handle(new HitCommand());
    }
}
