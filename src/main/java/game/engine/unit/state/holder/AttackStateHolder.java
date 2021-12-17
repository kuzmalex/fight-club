package game.engine.unit.state.holder;

import game.engine.unit.state.AttackState;

import java.util.Optional;

public class AttackStateHolder extends StateHolderWrapper{

    private static final double DEFAULT_STRENGTH = 100;
    private final AttackState attackState;

    public AttackStateHolder(StateHolder stateHolder, AttackState attackState){
        super(stateHolder);
        this.attackState = attackState;
    }

    private AttackStateHolder(StateHolder stateHolder) {
        super(stateHolder);
        this.attackState = new AttackState(DEFAULT_STRENGTH);
    }

    @Override
    public Optional<AttackState> getAttackState(){
        return Optional.of(attackState);
    }
}
