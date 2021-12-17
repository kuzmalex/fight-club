package server;

import com.sun.net.httpserver.*;
import infostructure.di.annotations.AfterContextInitialization;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

public class Server {

    private final HttpsServer server;
    private final int port;

    public Server(
            Map<String, HttpHandler> handlerMapping,
            List<Filter> filters,
            SSLContext sslContext,
            ExecutorService executorService,
            int port
    ) {
        this.port = port;

        try {
            server = HttpsServer.create(new InetSocketAddress(port), 30);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        server.setHttpsConfigurator(new HttpsConfigurator(sslContext) {
            public void configure (HttpsParameters params) {
                SSLContext context = getSSLContext();
                SSLParameters sslParams = context.getDefaultSSLParameters();
                params.setSSLParameters(sslParams);
            }
        });

        handlerMapping.forEach(
                (path, handler) -> {
                    HttpContext context = server.createContext(path, handler);
                    context.getFilters().addAll(filters);
                }
        );

        server.setExecutor(executorService);
    }

    @AfterContextInitialization
    private void start(){
        server.start();
        System.out.println("Server started at " + port);
    }
}
