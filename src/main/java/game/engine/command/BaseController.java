package game.engine.command;

import game.engine.event.HitEvent;
import game.engine.unit.Player;
import game.engine.unit.Unit;

import java.util.HashSet;
import java.util.Set;

public class BaseController implements Controller {

    private final Player player;
    private final Set<Unit> commandedUnits;

    public BaseController(Player player) {
        this.player = player;
        commandedUnits = new HashSet<>();
    }

    public void addCommandedUnit(Unit unit){
        commandedUnits.add(unit);
    }

    public void removeCommandedUnit(Unit unit){
        commandedUnits.remove(unit);
    }

    @Override
    public void handle(HitCommand hitCommand) {
        commandedUnits.forEach(unit -> {
            long time = System.currentTimeMillis();
            unit.handle(new HitEvent(time));
        });
    }

    public Player getPlayer() {
        return player;
    }
}
