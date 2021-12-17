package game.engine.unit;

public class Player {

    public static Player defaultPlayer = new Player("Default");

    private final String name;

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
