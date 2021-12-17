package game;

import game.engine.command.Controller;
import game.engine.unit.Unit;
import game.engine.unit.UnitId;
import game.event.GameFinishEvent;

import java.util.HashMap;
import java.util.List;

public interface Game {
    void update();
    HashMap<UnitId, Unit> getUnitsById();
    List<Unit> getUnits();
    List<Controller> getControllers();
    boolean isFinished();

    void addListener(GameListener gameListener);
    void notify(GameFinishEvent event);
}
