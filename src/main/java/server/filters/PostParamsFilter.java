package server.filters;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;
import server.RequestContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PostParamsFilter extends Filter {

    private final RequestContext requestContext;

    public PostParamsFilter(RequestContext requestContext) {
        this.requestContext = requestContext;
    }

    @Override
    public void doFilter(HttpExchange exchange, Chain chain) throws IOException {
        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr);

        String query = br.readLine();
        if (query != null) {
            Map<String, String> parameters = parseQuery(query);
            requestContext.getRequestParams().putAll(parameters);
        }

        chain.doFilter(exchange);
    }

    public Map<String, String> parseQuery(String query) {
        Objects.requireNonNull(query);
        String[] components = query.split("&");
        Map<String, String> parameters = new HashMap<>();
        for (String component : components) {
            String[] pieces = component.split("=");
            parameters.put(pieces[0], pieces[1]);
        }
        return parameters;
    }

    @Override
    public String description() {
        return "PostParamsFilter";
    }
}
