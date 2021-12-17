package game;

import game.event.GameFinishEvent;

public interface GameListener {
    void accept(GameFinishEvent event);
}
