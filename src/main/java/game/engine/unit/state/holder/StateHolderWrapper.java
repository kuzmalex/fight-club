package game.engine.unit.state.holder;

import game.engine.unit.state.AttackState;
import game.engine.unit.state.HealthState;
import game.engine.unit.state.MortalState;

import java.util.Optional;

public abstract class StateHolderWrapper implements StateHolder {
    protected StateHolder stateHolder;

    public StateHolderWrapper(StateHolder stateHolder){
        this.stateHolder = stateHolder;
    }

    @Override
    public Optional<HealthState> getHealthState() {
        return stateHolder.getHealthState();
    }

    @Override
    public Optional<AttackState> getAttackState() {
        return stateHolder.getAttackState();
    }

    @Override
    public Optional<MortalState> getMortalState() {
        return stateHolder.getMortalState();
    }

    public StateHolder getStateHolder() {
        return stateHolder;
    }

    public void setStateHolder(StateHolder stateHolder) {
        this.stateHolder = stateHolder;
    }
}
