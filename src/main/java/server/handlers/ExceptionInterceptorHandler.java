package server.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import server.handlers.exception.InvalidPasswordException;

import java.io.IOException;

public class ExceptionInterceptorHandler implements HttpHandler {

    private final HttpHandler httpHandler;

    public ExceptionInterceptorHandler(HttpHandler httpHandler) {
        this.httpHandler = httpHandler;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            httpHandler.handle(exchange);
        }
        catch (Exception e){
            if (e instanceof InvalidPasswordException){
                exchange.sendResponseHeaders(401, -1);
            }
            e.printStackTrace();
            exchange.sendResponseHeaders(500, -1);
        }
    }
}
