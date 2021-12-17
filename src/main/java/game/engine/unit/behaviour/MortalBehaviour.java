package game.engine.unit.behaviour;

import game.engine.event.DeathEvent;
import game.engine.unit.Unit;
import game.engine.unit.state.MortalState;

public class MortalBehaviour extends Behaviour {

    private final MortalState mortalState;

    public MortalBehaviour(Unit unit) {
        super(unit);
        this.mortalState = unit.getMortalState().orElseThrow();
    }

    @Override
    public void handle(DeathEvent event) {
        if (event.unit().equals(unit))
            mortalState.setDead(true);
    }
}
