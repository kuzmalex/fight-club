package game.engine.unit;

import game.engine.unit.behaviour.BehaviourFactory;

import java.util.function.Supplier;

public interface UnitFactoryBuilder {
    UnitFactoryBuilder withBehaviour(BehaviourFactory behaviourFactory);
    UnitFactoryBuilder belongsTo(Player player);
    UnitFactoryBuilder withIdSupplier(Supplier<UnitId> idSupplier);
    UnitFactoryBuilder ofType(UnitType unitType);
}
