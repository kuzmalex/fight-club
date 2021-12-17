package config.common;

import infostructure.di.annotations.Configuration;
import infostructure.di.annotations.ManagedObject;
import server.RequestContext;

import java.util.concurrent.*;

@Configuration
public class ThreadPoolConfig {

    @ManagedObject
    public ScheduledExecutorService taskScheduler(){
        return Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
    }

    @ManagedObject
    public ExecutorService executorServiceBean(RequestContext requestContext){

        return new ThreadPoolExecutor(
                Runtime.getRuntime().availableProcessors(),
                Runtime.getRuntime().availableProcessors(),
                1,
                TimeUnit.MINUTES,
                new LinkedBlockingQueue<>()
        ){
            @Override
            protected void afterExecute(Runnable r, Throwable t){
                requestContext.removeValues();
            }
        };
    }
}
