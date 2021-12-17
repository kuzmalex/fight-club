package game.engine.event;

import game.engine.unit.Unit;

public record DeathEvent(Unit unit) implements Event {

}
