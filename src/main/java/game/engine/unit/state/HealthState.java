package game.engine.unit.state;

public class HealthState {

    private double maxHealth;
    private double health;

    public HealthState(double health){
        this.health = health;
        this.maxHealth = health;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(double maxHealth) {
        this.maxHealth = maxHealth;
    }
}
