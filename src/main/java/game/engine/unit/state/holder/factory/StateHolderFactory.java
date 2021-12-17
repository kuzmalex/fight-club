package game.engine.unit.state.holder.factory;

import game.engine.unit.state.holder.StateHolder;

public interface StateHolderFactory {
    StateHolder create(StateHolder unit);
}
