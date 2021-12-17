package game.engine.unit.logger;

import game.Game;
import game.engine.event.DamageEvent;
import game.engine.event.DefaultEventConsumer;
import game.engine.unit.Unit;
import game.engine.unit.UnitId;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.Function;

public class EventsLoggerImpl implements EventsLogger {

    private final ConcurrentLinkedDeque<Function<UnitId, String>> recordCallbacks = new ConcurrentLinkedDeque<>();
    private final int maxLogLength;

    public EventsLoggerImpl(Game game, int maxLogLength){
        this.maxLogLength = maxLogLength;
        listen(game.getUnits());
    }

    public List<String> buildUnitLog(UnitId unitId){
        return recordCallbacks.stream()
                .map(callBack -> callBack.apply(unitId))
                .limit(maxLogLength)
                .toList();
    }

    private void listen(List<Unit> units){
        units.forEach(unit -> unit.addListener(new DefaultEventConsumer() {
            @Override
            public void handle(DamageEvent damageEvent) {
                while (recordCallbacks.size() > maxLogLength){
                    recordCallbacks.pollLast();
                }
                recordCallbacks.addFirst(loggedUnitId -> toRecord(damageEvent, unit, loggedUnitId));
            }
        }));
    }

    private String toRecord(DamageEvent event, Unit eventUnit, UnitId loggedUnitId){
        boolean isUnitDamaged = loggedUnitId.equals(eventUnit.getId());

        Unit sourceOfDamage = event.getDamageSource();
        boolean isUnitTheSourceOfDamage = loggedUnitId.equals(sourceOfDamage.getId());

        return (isUnitTheSourceOfDamage ? "Вы" : sourceOfDamage.getName()) +
                (isUnitTheSourceOfDamage ? " ударили " : " ударил ") +
                (isUnitDamaged ? "Вас" : eventUnit.getName()) +
                " на " + event.getDamage() + " урона";
    }
}
