package game.engine.event;

public interface EventConsumer {
    void handle(AttackEvent attackEvent);
    void handle(HitEvent hitEvent);
    void handle(DamageEvent damageEvent);
    void handle(DeathEvent deathEvent);
}
