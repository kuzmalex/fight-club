package server.handlers.request;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import domain.User;
import security.UserDetails;
import server.JWebToken;
import server.RequestContext;
import server.handlers.path.RootHandler;
import game.service.GameSessionService;
import server.service.UserService;

import java.io.IOException;

public class StartDuelRequestHandler implements HttpHandler {

    private final GameSessionService gameSessionService;
    private final UserService userService;

    private final RequestContext requestContext;

    private final RootHandler rootHandler;

    public StartDuelRequestHandler(
            GameSessionService gameSessionService,
            UserService userService,
            RequestContext requestContext,
            RootHandler rootHandler
    ) {
        this.gameSessionService = gameSessionService;
        this.userService = userService;
        this.requestContext = requestContext;
        this.rootHandler = rootHandler;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        JWebToken token = requestContext.getJWebToken();
        UserDetails userDetails = token.getDetails();

        User user = userService.getByName(userDetails.getSubject()).orElseThrow();
        gameSessionService.joinGame(user);

        rootHandler.handle(exchange);
    }
}
