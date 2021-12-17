package server.filters;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;

public class ExceptionInterceptorFilter extends Filter {

    @Override
    public void doFilter(HttpExchange exchange, Chain chain) {
        try {
            chain.doFilter(exchange);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public String description() {
        return "ExceptionInterceptorFilter";
    }
}
