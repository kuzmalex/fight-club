package server.handlers.request;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import domain.User;
import security.UserDetails;
import game.session.GameSession;
import server.JWebToken;
import server.RequestContext;
import server.handlers.path.RootHandler;
import game.service.GameSessionService;
import server.service.UserService;

import java.io.IOException;
import java.util.Optional;

public class HitRequestHandler implements HttpHandler {

    private final RequestContext requestContext;
    private final UserService userService;
    private final GameSessionService gameSessionService;
    private final RootHandler rootHandler;

    public HitRequestHandler(
            RequestContext requestContext,
            RootHandler rootHandler,
            UserService userService,
            GameSessionService gameSessionService
    ) {
        this.requestContext = requestContext;
        this.userService = userService;
        this.gameSessionService = gameSessionService;
        this.rootHandler = rootHandler;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        JWebToken token = requestContext.getJWebToken();
        UserDetails userDetails = token.getDetails();

        User user = userService.getByName(userDetails.getSubject()).orElseThrow();

        Optional<GameSession> session = gameSessionService.getSessionByUser(user);
        session.ifPresent(gameSession -> gameSession.getInputProcessor().hitBy(user));

        rootHandler.handle(exchange);
    }
}
