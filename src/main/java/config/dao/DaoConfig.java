package config.dao;

import dao.*;
import dao.cache.SortedUserCache;
import dao.cache.UserCountCache;
import dao.impl.PermissionDao;
import dao.impl.PlayerStatsDaoImpl;
import dao.impl.RoleDaoImpl;
import dao.impl.UserDaoImpl;
import dao.mapper.ResultSetMapper;
import dao.mapper.ResultSetMapperImpl;
import dao.transaction.TransactionManager;
import infostructure.di.annotations.Configuration;
import infostructure.di.annotations.ManagedObject;

import java.util.concurrent.ScheduledExecutorService;

@Configuration
public class DaoConfig {

    @ManagedObject
    public UserDao userDao(
            TransactionManager transactionManager,
            ResultSetMapper mapper,
            ScheduledExecutorService taskScheduler
    ){
        return new UserCountCache(
                new SortedUserCache(
                        new UserDaoImpl(transactionManager, mapper),
                        taskScheduler
                )
        );
    }

    @ManagedObject
    public PermissionDao permissionDao(TransactionManager transactionManager, ResultSetMapper mapper){
        return new PermissionDao(transactionManager, mapper);
    }

    @ManagedObject
    public PlayerStatsDao playerStatsDao(TransactionManager transactionManager, ResultSetMapper mapper){
        return new PlayerStatsDaoImpl(transactionManager, mapper);
    }

    @ManagedObject
    public RoleDao roleDao(TransactionManager transactionManager, ResultSetMapper mapper){
        return new RoleDaoImpl(transactionManager, mapper);
    }

    @ManagedObject
    public ResultSetMapper mapper(){
        return new ResultSetMapperImpl();
    }
}
