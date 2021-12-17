package server.handlers.request;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import domain.User;
import server.Cookie;
import server.JWebToken;
import server.RequestContext;
import server.handlers.path.RootHandler;
import security.service.AuthenticationService;
import server.service.UserService;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

public class LoginRequestHandler implements HttpHandler {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    private final RootHandler rootHandler;
    private final RequestContext requestContext;

    public LoginRequestHandler(
            UserService userService,
            RootHandler rootHandler,
            RequestContext requestContext,
            AuthenticationService authenticationService
    ) {
        this.userService = userService;
        this.rootHandler = rootHandler;
        this.requestContext = requestContext;
        this.authenticationService = authenticationService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        Map<String, String> parameters = requestContext.getRequestParams();
        String userName = parameters.get("user");
        String password = parameters.get("password");

        Optional<User> existingUser = userService.getByName(userName);
        if (existingUser.isEmpty()){
            existingUser = userService.register(userName, password);
        }

        if (existingUser.isPresent()){
            User user = existingUser.get();

            JWebToken jWebToken = authenticationService.authenticate(user, password);
            requestContext.setJWebToken(jWebToken);

            var tokenCookie = new Cookie(
                    "token",
                    jWebToken.toString(),
                    null,
                    Integer.MAX_VALUE,
                    null,
                    null,
                    true,
                    true,
                    null
            );
            exchange.getResponseHeaders().add("Set-Cookie", tokenCookie.toString());
        }

        rootHandler.handle(exchange);
    }
}
