package game.engine.unit.behaviour;

import game.engine.event.AttackEvent;
import game.engine.event.DamageEvent;
import game.engine.event.HitEvent;
import game.engine.unit.Unit;
import game.engine.unit.state.AttackState;

public class AttackBehaviour extends Behaviour {

    public static final long HIT_DELAY = 1000;
    private final AttackState attackState;

    public AttackBehaviour(Unit unit) {
        super(unit);
        this.attackState = unit.getAttackState().orElseThrow();
    }

    @Override
    public void handle(AttackEvent event) {
        attackState.setTarget(event.getTarget());
    }

    @Override
    public void handle(HitEvent event){
        Unit target = attackState.getTarget();
        long lastHitTime = attackState.getLastHitTime();
        double strength = attackState.getStrength();

        if (event.getTime() - lastHitTime >= HIT_DELAY) {
            target.handle(
                    new DamageEvent(unit, strength)
            );
            attackState.setLastHitTime(event.getTime());
        }
    }

    public double getStrength() {
        return attackState.getStrength();
    }

    public long getLastHit() {
        return attackState.getLastHitTime();
    }

    public Unit getTarget() {
        return attackState.getTarget();
    }
}
