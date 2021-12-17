package game.engine.unit;

import game.engine.GameObject;
import game.engine.event.*;
import game.engine.unit.behaviour.Behaviour;
import game.engine.unit.state.holder.StateHolder;
import game.engine.unit.state.holder.StateHolderWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Unit extends StateHolderWrapper implements GameObject, EventConsumer {

    private final List<Behaviour> behaviours = new ArrayList<>();
    private final List<EventConsumer> listeners = new ArrayList<>();
    private final ConcurrentLinkedDeque<Runnable> eventTasks = new ConcurrentLinkedDeque<>();

    private UnitId id;
    private UnitType unitType;
    private String name;
    private Player owner;

    public Unit(StateHolder stateHolder) {
        super(stateHolder);
    }

    @Override
    public void update() {
        handleEvents();
    }

    private void handleEvents(){
        while (!eventTasks.isEmpty()){
            Runnable task = eventTasks.poll();
            task.run();
        }
    }

    public void addBehaviour(Behaviour behaviour){
        behaviours.add(behaviour);
    }

    public void addListener(EventConsumer listener){
        listeners.add(listener);
    }

    @Override
    public void handle(DamageEvent event){
        eventTasks.add(() -> {
            behaviours.forEach(b -> b.handle(event));
            listeners.forEach(l -> l.handle(event));
        });
    }

    @Override
    public void handle(AttackEvent event){
        eventTasks.add(() -> {
            behaviours.forEach(b -> b.handle(event));
            listeners.forEach(l -> l.handle(event));
        });
    }

    @Override
    public void handle(HitEvent event) {
        eventTasks.add(() -> {
            behaviours.forEach(b -> b.handle(event));
            listeners.forEach(l -> l.handle(event));
        });
    }

    @Override
    public void handle(DeathEvent event){
        eventTasks.add(() -> {
            behaviours.forEach(b -> b.handle(event));
            listeners.forEach(l -> l.handle(event));
        });
    }

    protected void setId(UnitId id) {
        this.id = id;
    }

    public UnitId getId() {
        return id;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public List<Behaviour> getBehaviours() {
        return behaviours;
    }

    public UnitType getUnitType() {
        return unitType;
    }

    public void setUnitType(UnitType unitType) {
        this.unitType = unitType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Unit unit = (Unit) o;
        return id == unit.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
