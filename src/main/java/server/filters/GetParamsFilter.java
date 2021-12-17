package server.filters;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;
import server.RequestContext;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GetParamsFilter extends Filter {

    private final RequestContext requestContext;

    public GetParamsFilter(RequestContext requestContext) {
        this.requestContext = requestContext;
    }

    @Override
    public void doFilter(HttpExchange exchange, Chain chain) throws IOException {
        URI requestedUri = exchange.getRequestURI();
        String query = requestedUri.getRawQuery();
        if (query != null) {
            Map<String, String> params = parseParams(query);
            requestContext.getRequestParams().putAll(params);
        }
        chain.doFilter(exchange);
    }

    @Override
    public String description() {
        return "GetParamsFilter";
    }

    private Map<String, String> parseParams(String query){
        Objects.requireNonNull(query);

        Map<String, String> parsedPairs = new HashMap<>();

        if (query.isBlank()) return parsedPairs;

        String[] pairs = query.split("[&]");
        for (String pair : pairs) {
            String[] param = pair.split("[=]");

            if (param.length != 2)
                throw new RuntimeException("Invalid query param: " + pair);

            try {
                String key = URLDecoder.decode(param[0], System.getProperty("file.encoding"));
                String value = URLDecoder.decode(param[1], System.getProperty("file.encoding"));
                parsedPairs.put(key, value);

            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }

        return parsedPairs;
    }
}
