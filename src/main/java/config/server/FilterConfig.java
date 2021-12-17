package config.server;

import com.sun.net.httpserver.Filter;
import infostructure.di.annotations.Configuration;
import infostructure.di.annotations.ManagedObject;
import server.RequestContext;
import server.filters.*;
import security.service.AuthorizationService;
import server.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class FilterConfig {

    @ManagedObject(name="filters")
    public List<Filter> filters(
            RequestContext requestContext,
            AuthorizationService authorizationService,
            UserService userService
    ){

        PostParamsFilter postParamsFilter = new PostParamsFilter(requestContext);
        GetParamsFilter getParamsFilter = new GetParamsFilter(requestContext);

        List<Filter> filters = new ArrayList<>();
        filters.add(new ExceptionInterceptorFilter());
        filters.add(new CookieFilter(requestContext));
        filters.add(new RequestParamsFilter(postParamsFilter, getParamsFilter));
        filters.add(new AuthenticationFilter(requestContext, userService));
        filters.add(new AnonymousAuthenticationFilter(requestContext, authorizationService));
        filters.add(new AuthorizationFilter(authorizationService, requestContext));

        return filters;
    }
}
