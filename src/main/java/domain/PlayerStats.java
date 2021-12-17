package domain;

import dao.mapper.Column;

public class PlayerStats {
    @Column(name="health")
    private double health;
    @Column(name="strength")
    private double strength;
    @Column(name="userName")
    private String userName;

    public PlayerStats(){}

    public PlayerStats(String username, double health, double strength) {
        this.health = health;
        this.strength = strength;
        this.userName = username;
    }

    public double getHealth() {
        return health;
    }

    public double getStrength() {
        return strength;
    }

    public String getUserName() {
        return userName;
    }
}
