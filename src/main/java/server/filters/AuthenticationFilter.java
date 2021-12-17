package server.filters;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;
import server.Cookie;
import server.JWebToken;
import server.RequestContext;
import server.service.UserService;

import java.io.IOException;
import java.util.Map;

public class AuthenticationFilter extends Filter {

    private final RequestContext requestContext;
    private final UserService userService;

    public AuthenticationFilter(RequestContext requestContext, UserService userService) {
        this.requestContext = requestContext;
        this.userService = userService;
    }

    @Override
    public void doFilter(HttpExchange exchange, Chain chain) throws IOException {
        Map<String, String> cookies = requestContext.getCookies();

        String token = cookies.get(Cookie.JWT_COOKIE_NAME);
        if (token != null){

            JWebToken jwt = null;
            try {
                jwt = new JWebToken(token);
            } catch (Exception ignored){}

            if (jwt != null && jwt.isValid()){
                String userName = jwt.getDetails().getSubject();
                if (userService.getByName(userName).isPresent()){
                    requestContext.setJWebToken(jwt);
                }
            }
        }

        chain.doFilter(exchange);
    }

    @Override
    public String description() {
        return "AuthenticationFilter";
    }
}
