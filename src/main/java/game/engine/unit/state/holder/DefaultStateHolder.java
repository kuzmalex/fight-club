package game.engine.unit.state.holder;

import game.engine.unit.state.AttackState;
import game.engine.unit.state.HealthState;
import game.engine.unit.state.MortalState;

import java.util.Optional;

public interface DefaultStateHolder extends StateHolder{
    @Override
    default Optional<HealthState> getHealthState(){
        return Optional.empty();
    }
    @Override
    default Optional<AttackState> getAttackState(){
        return Optional.empty();
    };
    @Override
    default Optional<MortalState> getMortalState(){
        return Optional.empty();
    };
}
