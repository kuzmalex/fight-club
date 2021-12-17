package game.engine.unit.state.holder;

import game.engine.unit.state.AttackState;
import game.engine.unit.state.HealthState;
import game.engine.unit.state.MortalState;

import java.util.Optional;

public interface StateHolder {
    Optional<HealthState> getHealthState();
    Optional<AttackState> getAttackState();
    Optional<MortalState> getMortalState();
}
