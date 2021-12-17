package game.engine.event;

import game.engine.unit.Unit;

public class DamageEvent implements Event {

    private final double damage;
    private final Unit damageSource;

    public DamageEvent(Unit damageSource, double damage) {
        this.damage = damage;
        this.damageSource = damageSource;
    }

    public double getDamage() {
        return damage;
    }

    public Unit getDamageSource() {
        return damageSource;
    }
}
