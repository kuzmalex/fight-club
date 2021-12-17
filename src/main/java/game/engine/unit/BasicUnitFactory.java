package game.engine.unit;

import game.engine.unit.behaviour.BehaviourFactory;
import game.engine.unit.state.holder.DefaultStateHolder;
import game.engine.unit.state.holder.factory.StateHolderFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class BasicUnitFactory implements UnitFactory {

    protected static final AtomicInteger idSeries = new AtomicInteger(Integer.MIN_VALUE);
    protected final Supplier<UnitId> defaultIdSupplier = () -> new BasicUnitId(idSeries.incrementAndGet());

    protected List<BehaviourFactory> behaviourFactories = new ArrayList<>();
    protected List<StateHolderFactory> stateHolderFactories = new ArrayList<>();

    protected Supplier<UnitId> idSupplier = defaultIdSupplier;

    protected Player player = Player.defaultPlayer;
    protected UnitType unitType;
    protected Supplier<String> nameSupplier = () -> unitType.name();

    public Unit create() {
        Unit unit = new Unit(new DefaultStateHolder() {});
        unit.setId(idSupplier.get());
        unit.setOwner(player);
        unit.setUnitType(unitType);
        unit.setName(nameSupplier.get());
        stateHolderFactories.forEach(
                factory -> unit.setStateHolder(factory.create(unit.getStateHolder()))
        );
        behaviourFactories.forEach(
                factory -> unit.addBehaviour(factory.create(unit))
        );
        return unit;
    }

    static class BasicUnitId implements UnitId{
        int value;
        BasicUnitId(int value){
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            BasicUnitId that = (BasicUnitId) o;
            return value == that.value;
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }
    }
}
