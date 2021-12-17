package config.dao;

import infostructure.di.annotations.Configuration;
import infostructure.di.annotations.ManagedObject;
import org.flywaydb.core.Flyway;

import javax.sql.DataSource;

@Configuration
public class MigrationConfig {

    @ManagedObject
    public Flyway flyway(DataSource dataSource){
        return Flyway.configure()
                .baselineOnMigrate(true)
                .connectRetries(10)
                .dataSource(dataSource).load();
    }
}
