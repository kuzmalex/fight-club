package game.engine.command;

import game.Game;
import game.engine.unit.Player;
import game.engine.unit.Unit;

public class UpdateInvokeController implements Controller {

    private final Controller controller;
    private final Game game;

    public UpdateInvokeController(Controller controller, Game game) {
        this.controller = controller;
        this.game = game;
    }

    public void addCommandedUnit(Unit unit) {
        controller.addCommandedUnit(unit);
    }

    public void removeCommandedUnit(Unit unit) {
        controller.removeCommandedUnit(unit);
    }

    public void handle(HitCommand hitCommand) {
        controller.handle(hitCommand);
        game.update();
    }

    public Player getPlayer() {
        return controller.getPlayer();
    }
}
