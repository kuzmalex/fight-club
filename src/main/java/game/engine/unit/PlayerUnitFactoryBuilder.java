package game.engine.unit;

import game.engine.unit.behaviour.MortalBehaviour;
import game.engine.unit.state.AttackState;
import game.engine.unit.state.HealthState;
import game.engine.unit.behaviour.AttackBehaviour;
import game.engine.unit.behaviour.BehaviourFactory;
import game.engine.unit.behaviour.HealthBehavior;
import game.engine.unit.state.MortalState;
import game.engine.unit.state.holder.AttackStateHolder;
import game.engine.unit.state.holder.HealthStateHolder;
import game.engine.unit.state.holder.MortalStateHolder;

import java.util.function.Supplier;

public class PlayerUnitFactoryBuilder implements UnitFactoryBuilder {

    private final BasicUnitFactoryBuilder builder;

    public PlayerUnitFactoryBuilder(){
        builder = new BasicUnitFactoryBuilder();
    }

    public PlayerUnitFactoryBuilder withBehaviour(BehaviourFactory behaviourFactory){
        builder.withBehaviour(behaviourFactory);
        return this;
    }

    @Override
    public PlayerUnitFactoryBuilder belongsTo(Player player) {
        builder.belongsTo(player);
        return this;
    }

    public PlayerUnitFactoryBuilder withIdSupplier(Supplier<UnitId> idSupplier){
        builder.withIdSupplier(idSupplier);
        return this;
    }

    public PlayerUnitFactoryBuilder withNameSupplier(Supplier<String> nameSupplier){
        builder.withNameSupplier(nameSupplier);
        return this;
    }

    @Override
    public PlayerUnitFactoryBuilder ofType(UnitType unitType) {
        builder.ofType(unitType);
        return this;
    }

    public PlayerUnitFactoryBuilder withHealth(double health){
        builder.withState(
                unit -> new HealthStateHolder(unit, new HealthState(health))
        );
        builder.withBehaviour(HealthBehavior::new);
        return this;
    }

    public PlayerUnitFactoryBuilder withStrength(double strength){
        builder.withState(
                unit -> new AttackStateHolder(unit, new AttackState(strength))
        );
        builder.withBehaviour(AttackBehaviour::new);
        return this;
    }

    public PlayerUnitFactoryBuilder mortal() {
        builder.withState(
                unit -> new MortalStateHolder(unit, new MortalState())
        );
        builder.withBehaviour(MortalBehaviour::new);
        return this;
    }

    public BasicUnitFactory build(){
        return builder.build();
    }
}
