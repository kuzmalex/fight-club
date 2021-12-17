package game;

import game.engine.command.Controller;
import game.engine.command.UpdateInvokeController;
import game.engine.event.*;
import game.engine.unit.Unit;
import game.engine.unit.UnitId;
import game.engine.unit.UnitType;
import game.event.GameFinishEvent;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class DuelGame implements Game {

    private final Game game;
    private final Unit firstPlayerUnit;
    private final Unit secondPlayerUnit;
    private final UpdateInvoker updateInvoker;


    public DuelGame(List<Unit> units, List<Controller> controllers){
        this(units.get(0), units.get(1), controllers);
        if (units.size() != 2){
            throw new RuntimeException("Unexpected units number: " + units.size());
        }
    }

    public DuelGame(Unit firstPlayerUnit, Unit secondPlayerUnit, List<Controller> controllers){

        this.firstPlayerUnit = firstPlayerUnit;
        this.secondPlayerUnit = secondPlayerUnit;

        if (firstPlayerUnit.getOwner().equals(secondPlayerUnit.getOwner())){
            throw new RuntimeException("Units must belong to different players.");
        }

        var endCondition = new DuelEndCondition();
        firstPlayerUnit.addListener(endCondition);
        secondPlayerUnit.addListener(endCondition);

        List<Controller> updateInvokeControllers = controllers.stream()
                .map(c-> new UpdateInvokeController(c, this))
                .collect(Collectors.toList());

        game = new BaseGame(List.of(firstPlayerUnit, secondPlayerUnit), updateInvokeControllers, endCondition);

        updateInvoker = new UpdateInvoker(game);

        firstPlayerUnit.handle(new AttackEvent(secondPlayerUnit));
        secondPlayerUnit.handle(new AttackEvent(firstPlayerUnit));

        update();
    }

    private static class DuelEndCondition implements GameEndCondition, DefaultEventConsumer {

        volatile boolean gameFinished = false;

        @Override
        public void handle(DeathEvent event) {
            if (event.unit().getUnitType().equals(UnitType.PLAYER)){
                gameFinished = true;
            }
        }

        @Override
        public boolean isSatisfied() {
            return gameFinished;
        }
    }

    private static class UpdateInvoker implements EventConsumer {

        Game game;
        boolean newEvents = false;

        public UpdateInvoker(Game game) {
            this.game = game;
            game.getUnits().forEach(u->u.addListener(this));
        }

        public void update(){
            game.update();
            while (newEvents){
                newEvents = false;
                game.update();
            }
        }

        @Override
        public void handle(AttackEvent attackEvent) {
            newEvents = true;
        }
        @Override
        public void handle(HitEvent hitEvent) {
            newEvents = true;
        }
        @Override
        public void handle(DamageEvent damageEvent) {
            newEvents = true;
        }
        @Override
        public void handle(DeathEvent deathEvent) {
            newEvents = true;
        }
    }

    public void update(){
        updateInvoker.update();
    }

    public HashMap<UnitId, Unit> getUnitsById() {
        return game.getUnitsById();
    }

    public List<Unit> getUnits() {
        return game.getUnits();
    }

    @Override
    public List<Controller> getControllers() {
        return game.getControllers();
    }

    @Override
    public boolean isFinished() {
        return game.isFinished();
    }

    @Override
    public void addListener(GameListener gameListener) {
        game.addListener(gameListener);
    }

    @Override
    public void notify(GameFinishEvent event) {
        game.notify(event);
    }

    public Unit getFirstPlayerUnit() {
        return firstPlayerUnit;
    }

    public Unit getSecondPlayerUnit() {
        return secondPlayerUnit;
    }
}
