package game.engine.unit;

import game.engine.unit.behaviour.BehaviourFactory;
import game.engine.unit.state.holder.factory.StateHolderFactory;

import java.util.function.Supplier;

public class BasicUnitFactoryBuilder implements UnitFactoryBuilder{

    private final BasicUnitFactory factory = new BasicUnitFactory();

    public UnitFactoryBuilder withBehaviour(BehaviourFactory behaviourFactory){
        factory.behaviourFactories.add(behaviourFactory);
        return this;
    }

    public UnitFactoryBuilder belongsTo(Player player){
        factory.player = player;
        return this;
    }

    public UnitFactoryBuilder ofType(UnitType unitType){
        factory.unitType = unitType;
        return this;
    }

    public UnitFactoryBuilder withIdSupplier(Supplier<UnitId> idSupplier){
        factory.idSupplier = idSupplier;
        return this;
    }

    public UnitFactoryBuilder withNameSupplier(Supplier<String> nameSupplier){
        factory.nameSupplier = nameSupplier;
        return this;
    }

    public UnitFactoryBuilder withState(StateHolderFactory holderFactory){
        factory.stateHolderFactories.add(holderFactory);
        return this;
    }

    public BasicUnitFactory build(){
        return factory;
    }
}
