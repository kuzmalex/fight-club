package game;

import game.engine.unit.Player;

import java.util.LinkedHashSet;

public record GameResult(LinkedHashSet<Player> winners, LinkedHashSet<Player> losers, long time) {

}
