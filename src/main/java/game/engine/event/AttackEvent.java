package game.engine.event;

import game.engine.unit.Unit;

public class AttackEvent implements Event {

    private final Unit target;

    public AttackEvent(Unit target) {
        this.target = target;
    }

    public Unit getTarget() {
        return target;
    }
}
