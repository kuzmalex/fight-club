package config.server;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpHandler;
import infostructure.di.annotations.Configuration;
import infostructure.di.annotations.ManagedObject;
import infostructure.di.annotations.Named;
import infostructure.di.annotations.Value;
import server.RequestContext;
import server.Server;

import javax.net.ssl.SSLContext;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

@Configuration
public class ServerConfig {

    @ManagedObject
    public Server server(
            @Named("httpHandlerMapping") Map<String, HttpHandler> handlerMapping,
            @Named("filters") List<Filter> filters,
            SSLContext sslContext,
            ExecutorService executorService,
            @Value("server_port") String portValue
    ){
        int port = Integer.parseInt(portValue);
        return new Server(handlerMapping, filters, sslContext, executorService, port);
    }

    @ManagedObject
    public RequestContext requestContext(){
        return new RequestContext();
    }
}
