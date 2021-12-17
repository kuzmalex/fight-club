package game.engine.event;

public class HitEvent implements Event{

    long time;

    public HitEvent(long time) {
        this.time = time;
    }

    public long getTime() {
        return time;
    }
}
