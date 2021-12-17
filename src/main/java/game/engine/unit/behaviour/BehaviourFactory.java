package game.engine.unit.behaviour;

import game.engine.unit.Unit;

@FunctionalInterface
public interface BehaviourFactory {
    Behaviour create(Unit unit);
}
