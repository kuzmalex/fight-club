package game.engine.command;

public interface CommandConsumer {
    void handle(HitCommand hitCommand);
}