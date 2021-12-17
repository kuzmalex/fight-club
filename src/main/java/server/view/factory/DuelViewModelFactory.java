package server.view.factory;

import domain.User;
import game.Game;
import game.engine.unit.Unit;
import game.engine.unit.UnitId;
import game.engine.unit.UnitType;
import game.engine.unit.behaviour.AttackBehaviour;
import game.engine.unit.logger.EventsLogger;
import game.engine.unit.state.AttackState;
import game.engine.unit.state.HealthState;
import game.session.GameSession;
import server.view.model.dto.UnitViewModel;
import server.view.model.page.DuelViewModel;
import game.service.GameSessionService;

import java.util.List;

public class DuelViewModelFactory implements ViewModelFactory<DuelViewModel>{

    private final GameSessionService gameSessionService;

    public DuelViewModelFactory(GameSessionService gameSessionService) {
        this.gameSessionService = gameSessionService;
    }

    @Override
    public DuelViewModel create(User user) {

        GameSession gameSession = gameSessionService.getSessionByUser(user).orElseThrow();
        Game game = gameSession.getGame();
        List<Unit> units = game.getUnits();

        Unit allyUnit = units.stream()
                .filter(u->u.getUnitType().equals(UnitType.PLAYER))
                .filter(u->u.getOwner().getName().equals(user.getName()))
                .findFirst()
                .orElseThrow();

        AttackState allyAttackState = allyUnit.getAttackState().orElseThrow();

        UnitId targetId = allyAttackState.getTarget().getId();
        Unit enemyUnit = units.stream()
                .filter(u->u.getId().equals(targetId))
                .findFirst()
                .orElseThrow();

        HealthState allyHealthState = allyUnit.getHealthState().orElseThrow();
        HealthState enemyHealthState = enemyUnit.getHealthState().orElseThrow();

        AttackState enemyAttackState = enemyUnit.getAttackState().orElseThrow();

        EventsLogger eventsLogger = gameSession.getEventsLogger();

        boolean canHit = System.currentTimeMillis() - allyAttackState.getLastHitTime() > AttackBehaviour.HIT_DELAY;

        UnitViewModel allyUnitModel = new UnitViewModel(
                allyUnit.getName(),
                allyHealthState.getHealth(),
                allyHealthState.getMaxHealth(),
                allyAttackState.getStrength()
        );

        UnitViewModel enemyUnitModel = new UnitViewModel(
                enemyUnit.getName(),
                enemyHealthState.getHealth(),
                enemyHealthState.getMaxHealth(),
                enemyAttackState.getStrength()
        );

        return new DuelViewModel(
                allyUnitModel,
                enemyUnitModel,
                eventsLogger.buildUnitLog(allyUnit.getId()),
                canHit
        );
    }
}
