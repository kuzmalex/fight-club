package config.server;

import com.sun.net.httpserver.HttpHandler;
import infostructure.di.annotations.Configuration;
import infostructure.di.annotations.ManagedObject;
import server.RequestContext;
import server.handlers.ExceptionInterceptorHandler;
import server.handlers.path.RootHandler;
import server.handlers.request.*;
import server.view.ViewResolver;
import security.service.AuthenticationService;
import game.service.GameSessionService;
import server.service.ImageService;
import server.service.UserService;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class HandlerConfig {

    @ManagedObject(name = "httpHandlerMapping")
    public Map<String, HttpHandler> handlerMapping(
            ViewResolver viewResolver,
            RequestContext requestContext,
            UserService userService,
            AuthenticationService authenticationService,
            GameSessionService gameSessionService,
            ImageService imageService
    ){
        RootHandler rootHandler = new RootHandler(viewResolver, requestContext, userService);

        Map<String, HttpHandler> handlerMapping = new HashMap<>();
        handlerMapping.put("/", new ExceptionInterceptorHandler(rootHandler));
        handlerMapping.put("/login", new ExceptionInterceptorHandler(new LoginRequestHandler(userService, rootHandler, requestContext, authenticationService)));
        handlerMapping.put("/logout", new ExceptionInterceptorHandler(new LogoutRequestHandler(rootHandler)));
        handlerMapping.put("/hit", new ExceptionInterceptorHandler(new HitRequestHandler(requestContext, rootHandler, userService, gameSessionService)));
        handlerMapping.put("/start", new ExceptionInterceptorHandler(new StartDuelRequestHandler(gameSessionService, userService, requestContext, rootHandler)));
        handlerMapping.put("/images", new ExceptionInterceptorHandler(new ImageRequestHandler(imageService)));

        return handlerMapping;
    }
}
