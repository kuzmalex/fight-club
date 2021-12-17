package game;

import game.engine.command.Controller;
import game.engine.unit.Unit;
import game.engine.unit.UnitId;
import game.event.GameFinishEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BaseGame implements Game {

    private final List<Unit> units;
    private final HashMap<UnitId, Unit> unitsById;
    private final List<Controller> controllers;
    private final List<GameListener> listeners;
    private final GameEndCondition gameEndCondition;
    private final Object updateLock = new Object();

    public BaseGame(List<Unit> units, List<Controller> controllers, GameEndCondition gameEndCondition){
        this.controllers = controllers;
        this.gameEndCondition = gameEndCondition;
        this.units = units;

        unitsById = new HashMap<>();
        units.forEach(unit->unitsById.put(unit.getId(), unit));

        listeners = new ArrayList<>();
    }

    @Override
    public void update() {
        synchronized (updateLock) {
            if (!isFinished()){
                units.forEach(Unit::update);
                if (isFinished()){
                    notify(new GameFinishEvent());
                }
            }
        }
    }

    public boolean isFinished() {
        return gameEndCondition.isSatisfied();
    }

    @Override
    public HashMap<UnitId, Unit> getUnitsById() {
        return unitsById;
    }

    @Override
    public List<Unit> getUnits() {
        return units;
    }

    @Override
    public List<Controller> getControllers() {
        return controllers;
    }

    @Override
    public void addListener(GameListener gameListener) {
        listeners.add(gameListener);
    }

    @Override
    public void notify(GameFinishEvent event) {
        listeners.forEach(l->l.accept(event));
    }
}
