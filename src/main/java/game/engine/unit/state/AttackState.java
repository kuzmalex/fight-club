package game.engine.unit.state;

import game.engine.unit.Unit;

public class AttackState {

    private final double strength;
    private Unit target;
    private long lastHitTime = 0;

    public AttackState(double strength) {
        this.strength = strength;
    }

    public double getStrength() {
        return strength;
    }

    public Unit getTarget() {
        return target;
    }

    public void setTarget(Unit target) {
        this.target = target;
    }

    public long getLastHitTime() {
        return lastHitTime;
    }

    public void setLastHitTime(long lastHitTime) {
        this.lastHitTime = lastHitTime;
    }
}
