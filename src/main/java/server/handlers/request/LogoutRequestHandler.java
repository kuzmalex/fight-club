package server.handlers.request;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import server.Cookie;
import server.handlers.path.RootHandler;

import java.io.IOException;

public class LogoutRequestHandler implements HttpHandler {

    private final RootHandler rootHandler;

    public LogoutRequestHandler(RootHandler rootHandler) {
        this.rootHandler = rootHandler;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        Cookie tokenCookie = new Cookie(
                "token",
                "",
                null,
                -1,
                null,
                null,
                true,
                true,
                null
        );

        exchange.getResponseHeaders().add("Set-Cookie", tokenCookie.toString());

        rootHandler.handle(exchange);
    }
}
