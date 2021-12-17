package game;

import game.engine.unit.Player;
import game.engine.unit.Unit;

import java.util.LinkedHashSet;
import java.util.concurrent.TimeUnit;

public class DuelGameResultFactory implements GameResultFactory{

    @Override
    public GameResult create(Game game) {

        if (!game.isFinished()){
            throw new RuntimeException("Can't create GameResult. Game has not finished.");
        }

        if (game instanceof DuelGame duelGame){

            Unit firstPlayerUnit = duelGame.getFirstPlayerUnit();
            Unit secondPlayerUnit = duelGame.getSecondPlayerUnit();

            boolean isFirstPlayerDead =
                    firstPlayerUnit
                            .getMortalState()
                            .orElseThrow()
                            .isDead();

            boolean isSecondPlayerDead =
                    secondPlayerUnit
                            .getMortalState()
                            .orElseThrow()
                            .isDead();

            if (isFirstPlayerDead == isSecondPlayerDead){
                throw new RuntimeException("Players cant be dead or alive both in the same time");
            }

            Player winner = isFirstPlayerDead ? secondPlayerUnit.getOwner() : firstPlayerUnit.getOwner();
            Player loser = isSecondPlayerDead ? firstPlayerUnit.getOwner() : secondPlayerUnit.getOwner();

            LinkedHashSet<Player> winners = new LinkedHashSet<>();
            winners.add(winner);

            LinkedHashSet<Player> losers = new LinkedHashSet<>();
            losers.add(loser);

            long time = TimeUnit.NANOSECONDS.toSeconds(System.nanoTime());

            return new GameResult(winners, losers, time);
        }

        throw new RuntimeException("Game must be DualGame type");
    }
}
