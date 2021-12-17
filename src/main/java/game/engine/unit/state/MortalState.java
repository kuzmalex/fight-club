package game.engine.unit.state;

public class MortalState {
    private boolean isDead = false;

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }
}
