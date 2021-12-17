package game.engine.unit.logger;

import game.engine.unit.UnitId;

import java.util.List;

public interface EventsLogger {
    List<String> buildUnitLog(UnitId unitId);
}
