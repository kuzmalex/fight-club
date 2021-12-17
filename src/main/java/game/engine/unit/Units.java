package game.engine.unit;

import domain.PlayerStats;

public class Units {

    public static Unit of(Player player, PlayerStats playerStats){
        UnitFactory unitFactory = new PlayerUnitFactoryBuilder()
                .belongsTo(player)
                .withNameSupplier(player::getName)
                .ofType(UnitType.PLAYER)
                .withHealth(playerStats.getHealth())
                .withStrength(playerStats.getStrength())
                .mortal()
                .build();
        return unitFactory.create();
    }
}
