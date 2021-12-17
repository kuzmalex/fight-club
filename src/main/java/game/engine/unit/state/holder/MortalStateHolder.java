package game.engine.unit.state.holder;

import game.engine.unit.state.MortalState;

import java.util.Optional;

public class MortalStateHolder extends StateHolderWrapper {

    private final MortalState mortalState;

    public MortalStateHolder(StateHolder stateHolder, MortalState mortalState){
        super(stateHolder);
        this.mortalState = mortalState;
    }

    private MortalStateHolder(StateHolder stateHolder) {
        super(stateHolder);
        this.mortalState = new MortalState();
    }

    @Override
    public Optional<MortalState> getMortalState(){
        return Optional.of(mortalState);
    }
}
