package game.engine.unit.behaviour;

import game.engine.unit.state.HealthState;
import game.engine.event.DamageEvent;
import game.engine.event.DeathEvent;
import game.engine.unit.Unit;

public class HealthBehavior extends Behaviour {

    private final HealthState healthState;

    public HealthBehavior(Unit unit) {
        super(unit);
        this.healthState = unit.getHealthState().orElseThrow();
    }

    @Override
    public void handle(DamageEvent event) {
        double health = getHealthState();
        health-=Math.min(event.getDamage(), health);
        if (health == 0){
            unit.handle(new DeathEvent(unit));
        }
        healthState.setHealth(health);
    }

    public double getHealthState() {
        return healthState.getHealth();
    }
}
