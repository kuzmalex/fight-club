package server.filters;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class RequestParamsFilter extends Filter {

    private final static String GET_METHOD = "GET";
    private final static String POST_METHOD = "POST";

    private final PostParamsFilter postFilter;
    private final GetParamsFilter getFilter;

    public RequestParamsFilter(PostParamsFilter postFilter, GetParamsFilter getFilter) {
        this.postFilter = postFilter;
        this.getFilter = getFilter;
    }

    @Override
    public void doFilter(HttpExchange exchange, Chain chain) throws IOException {
        switch (exchange.getRequestMethod()) {
            case (GET_METHOD) -> getFilter.doFilter(exchange, chain);
            case (POST_METHOD) -> postFilter.doFilter(exchange, chain);
            default -> throw new RuntimeException(exchange.getRequestMethod() + " method not supported");
        }
    }

    @Override
    public String description() {
        return null;
    }
}
