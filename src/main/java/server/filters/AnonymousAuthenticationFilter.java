package server.filters;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;
import domain.User;
import security.UserDetails;
import server.*;
import security.service.AuthorizationService;

import java.io.IOException;

public class AnonymousAuthenticationFilter extends Filter {

    private final RequestContext requestContext;
    private final AuthorizationService authorizationService;

    public AnonymousAuthenticationFilter(RequestContext requestContext, AuthorizationService authorizationService) {
        this.requestContext = requestContext;
        this.authorizationService = authorizationService;
    }

    @Override
    public void doFilter(HttpExchange exchange, Chain chain) throws IOException {
        JWebToken token = requestContext.getJWebToken();
        if (token == null){
            UserDetails anonymousDetails = new UserDetails(
                    User.GUEST_USER_NAME,
                    authorizationService.getGuestRole(),
                    Long.MAX_VALUE
            );
            token = new JWebToken(anonymousDetails);
            requestContext.setJWebToken(token);
        }
        chain.doFilter(exchange);
    }

    @Override
    public String description() {
        return "AnonymousAuthenticationFilter";
    }
}
