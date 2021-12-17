package game;

import dao.PlayerStatsDao;
import domain.PlayerStats;
import domain.User;
import game.engine.command.BaseController;
import game.engine.command.Controller;
import game.engine.unit.Player;
import game.engine.unit.Unit;
import game.engine.unit.Units;
import game.session.MatchedTeam;

import java.util.ArrayList;
import java.util.List;

public class DuelGameFactory implements GameFactory {

    private final PlayerStatsDao playerStatsDao;

    public DuelGameFactory(PlayerStatsDao playerStatsDao) {
        this.playerStatsDao = playerStatsDao;
    }

    @Override
    public Game create(List<MatchedTeam> teams) {

        List<Unit> units = new ArrayList<>();
        List<Controller> controllers = new ArrayList<>();

        for (MatchedTeam matchedTeam : teams){

            for (User user : matchedTeam.getUsers()){
                PlayerStats playerStats = playerStatsDao.findByUserName(user.getName()).orElseThrow();
                Player player = new Player(user.getName());

                Unit unit = Units.of(player, playerStats);
                units.add(unit);

                Controller controller = new BaseController(player);
                controller.addCommandedUnit(unit);
                controllers.add(controller);
            }
        }

        return new DuelGame(units, controllers);
    }
}
