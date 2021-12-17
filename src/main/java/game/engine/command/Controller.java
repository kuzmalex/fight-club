package game.engine.command;

import game.engine.unit.Player;
import game.engine.unit.Unit;

public interface Controller extends CommandConsumer {
    void addCommandedUnit(Unit unit);
    void removeCommandedUnit(Unit unit);
    Player getPlayer();
}
