package server.view.condition;

import domain.User;
import server.JWebToken;
import server.RequestContext;
import security.service.AuthorizationService;

public class LoginViewCondition implements ViewCondition {

    private final RequestContext requestContext;
    private final AuthorizationService authorizationService;

    public LoginViewCondition(RequestContext requestContext, AuthorizationService authorizationService) {
        this.requestContext = requestContext;
        this.authorizationService = authorizationService;
    }

    @Override
    public boolean isSatisfied(User unused) {
        JWebToken token = requestContext.getJWebToken();
        return authorizationService.isGuest(token.getDetails());
    }
}
