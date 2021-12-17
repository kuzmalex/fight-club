package config.dao;

import com.mysql.cj.jdbc.MysqlDataSource;
import infostructure.di.annotations.Configuration;
import infostructure.di.annotations.ManagedObject;
import infostructure.di.annotations.Value;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @ManagedObject
    public DataSource mysqlDataSource(
            @Value("db_host") String host,
            @Value("db_port") String port,
            @Value("db_schema") String schema,
            @Value("db_user") String user,
            @Value("db_password") String password
    ){
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL("jdbc:mysql://"+host+":"+port+"/"+schema);
        dataSource.setUser(user);
        dataSource.setPassword(password);

        return dataSource;
    }
}
