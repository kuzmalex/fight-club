package game.session;

public interface GameSessionFactory {
    GameSession create(Match match);
}
