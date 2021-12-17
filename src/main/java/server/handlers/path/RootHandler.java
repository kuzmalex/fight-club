package server.handlers.path;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import domain.User;
import security.UserDetails;
import server.JWebToken;
import server.RequestContext;
import server.view.ViewResolver;
import server.service.UserService;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class RootHandler implements HttpHandler {

    private final int BUFFER_SIZE = 4096;

    private final ViewResolver viewResolver;
    private final RequestContext requestContext;
    private final UserService userService;

    public RootHandler(ViewResolver viewResolver, RequestContext requestContext, UserService userService) {
        this.viewResolver = viewResolver;
        this.requestContext = requestContext;
        this.userService = userService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        JWebToken token = requestContext.getJWebToken();
        UserDetails userDetails = token.getDetails();

        User user = userService.getByName(userDetails.getSubject()).orElseThrow();
        String response = viewResolver.getPage(user);

        exchange.sendResponseHeaders(200, 0);

        try (BufferedOutputStream out = new BufferedOutputStream(exchange.getResponseBody())) {
            try (ByteArrayInputStream bis = new ByteArrayInputStream(response.getBytes())) {
                byte [] buffer = new byte [BUFFER_SIZE];
                int count ;
                while ((count = bis.read(buffer)) != -1) {
                    out.write(buffer, 0, count);
                }
                out.close();
            }
        }
    }
}
