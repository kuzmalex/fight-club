package server.view.model.dto;

public record UnitViewModel(
        String name,
        double health,
        double maxHealth,
        double strength
) {}
