package game.engine.unit.behaviour;

import game.engine.event.DefaultEventConsumer;
import game.engine.unit.Unit;

public abstract class Behaviour implements DefaultEventConsumer {

    protected Unit unit;

    public Behaviour(Unit unit){
        this.unit = unit;
    }
}
