package server.filters;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import server.RequestContext;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CookieFilter extends Filter {

    private final RequestContext requestContext;

    public CookieFilter(RequestContext requestContext) {
        this.requestContext = requestContext;
    }

    @Override
    public void doFilter(HttpExchange exchange, Chain chain) throws IOException {
        Headers headers = exchange.getRequestHeaders();
        String cookieHeader = headers.getFirst("Cookie");
        if (cookieHeader != null) {
            Map<String, String> cookies = parseCookies(cookieHeader);
            requestContext.getCookies().putAll(cookies);
        }

        chain.doFilter(exchange);
    }

    @Override
    public String description() {
        return "CookieFilter";
    }

    private Map<String, String> parseCookies(String cookieString)  {
        Objects.requireNonNull(cookieString);

        Map<String, String> cookies = new HashMap<>();
        String[] cookiePairs = cookieString.split("; ");
        for (int i = 0; i < cookiePairs.length; i++)  {
            String[] cookieValue = cookiePairs[i].split("=");
            cookies.put(cookieValue[0], cookieValue[1]);
        }
        return cookies;
    }
}
