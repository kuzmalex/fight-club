package server.filters;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;
import server.JWebToken;
import server.RequestContext;
import security.service.AuthorizationService;

import java.io.IOException;

public class AuthorizationFilter extends Filter {

    private final AuthorizationService authorizationService;
    private final RequestContext requestContext;

    public AuthorizationFilter(AuthorizationService authorizationService, RequestContext requestContext) {
        this.authorizationService = authorizationService;
        this.requestContext = requestContext;
    }

    @Override
    public void doFilter(HttpExchange exchange, Chain chain) throws IOException {
        String path = exchange.getHttpContext().getPath();

        JWebToken token = requestContext.getJWebToken();
        if (token != null && authorizationService.isPermitted(token.getDetails(), path)){
            exchange.sendResponseHeaders(403, -1);
        }
        else chain.doFilter(exchange);
    }

    @Override
    public String description() {
        return "AuthorizationFilter";
    }
}
