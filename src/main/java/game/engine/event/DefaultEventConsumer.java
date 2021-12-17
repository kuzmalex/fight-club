package game.engine.event;

public interface DefaultEventConsumer extends EventConsumer{
    default void handle(AttackEvent attackEvent){}
    default void handle(DamageEvent damageEvent){}
    default void handle(DeathEvent deathEvent){}
    default void handle(HitEvent hitEvent){};
}
