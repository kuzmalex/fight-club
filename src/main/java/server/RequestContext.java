package server;

import domain.User;
import monitoring.ExecutionMetrics;

import java.util.HashMap;
import java.util.Map;

public class RequestContext {

    private final ThreadLocal<ExecutionMetrics> executionMetrics = ThreadLocal.withInitial(ExecutionMetrics::new);
    private final ThreadLocal<Map<String, String>> requestParams = ThreadLocal.withInitial(HashMap::new);
    private final ThreadLocal<Map<String, String>> cookies = ThreadLocal.withInitial(HashMap::new);
    private final ThreadLocal<JWebToken> jWebToken = ThreadLocal.withInitial(()->null);
    private final ThreadLocal<User> user = ThreadLocal.withInitial(()->null);

    public ExecutionMetrics getExecutionMetrics() {
        return executionMetrics.get();
    }

    public Map<String, String> getRequestParams() {
        return requestParams.get();
    }

    public Map<String, String> getCookies() {
        return cookies.get();
    }

    public JWebToken getJWebToken() {
        return jWebToken.get();
    }

    public void setJWebToken(JWebToken token){
        jWebToken.set(token);
    }

    public User getUser() {
        return user.get();
    }

    public void setUser(User user) {
        this.user.set(user);
    }

    public void removeValues(){
        executionMetrics.remove();
        requestParams.remove();
        cookies.remove();
        jWebToken.remove();
        user.remove();
    }

}
