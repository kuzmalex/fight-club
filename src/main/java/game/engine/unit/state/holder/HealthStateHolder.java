package game.engine.unit.state.holder;

import game.engine.unit.state.HealthState;

import java.util.Optional;

public class HealthStateHolder extends StateHolderWrapper{

    private static final double DEFAULT_HEALTH = 100;

    private final HealthState healthState;

    public HealthStateHolder(StateHolder stateHolder, HealthState healthState){
        super(stateHolder);
        this.healthState = healthState;
    }

    private HealthStateHolder(StateHolder stateHolder) {
        super(stateHolder);
        this.healthState = new HealthState(DEFAULT_HEALTH);
    }

    @Override
    public Optional<HealthState> getHealthState(){
        return Optional.of(healthState);
    }
}
