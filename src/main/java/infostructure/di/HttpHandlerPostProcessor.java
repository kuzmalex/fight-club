package infostructure.di;

import com.sun.net.httpserver.HttpHandler;
import server.handlers.ExceptionInterceptorHandler;

import java.util.HashMap;
import java.util.Map;

public class HttpHandlerPostProcessor implements PostProcessor {

    @Override
    public Object process(Object entity, String name) {
        if (name != null && name.equals("httpHandlerMapping")){

            var handlerMapping = (Map<String, HttpHandler>)entity;
            var processedHandlerMapping = new HashMap<String, HttpHandler>();
            handlerMapping.forEach((path, handler) -> {
                processedHandlerMapping.put(path, new ExceptionInterceptorHandler(handler));
            });

            return processedHandlerMapping;
        }
        return entity;
    }
}
