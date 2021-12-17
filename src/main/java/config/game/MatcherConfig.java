package config.game;

import game.session.DuelPlayerMatcher;
import game.session.PlayerMatcher;
import infostructure.di.annotations.Configuration;
import infostructure.di.annotations.ManagedObject;

@Configuration
public class MatcherConfig {

    @ManagedObject
    public PlayerMatcher playerMatcher(){
        return new DuelPlayerMatcher();
    }
}
