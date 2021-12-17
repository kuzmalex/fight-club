package server;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Cookie{

    public static final String JWT_COOKIE_NAME = "token";

    private final String name;
    private final String value;
    private final Date expires;
    private final Integer maxAge;
    private final String domain;
    private final String path;
    private final boolean secure;
    private final boolean httpOnly;
    private final String sameSite;

    public Cookie(
            String name,
            String value,
            Date expires,
            Integer maxAge,
            String domain,
            String path,
            boolean secure,
            boolean httpOnly,
            String sameSite
    ){
        this.name = name;
        this.value = value;
        this.expires = expires;
        this.maxAge = maxAge;
        this.domain = domain;
        this.path = path;
        this.secure = secure;
        this.httpOnly = httpOnly;
        this.sameSite = sameSite;
    }

    public String toString(){
        StringBuffer s = new StringBuffer();

        s.append(name).append("=").append(value);

        if (expires != null){
            SimpleDateFormat fmt = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
            s.append("; Expires=").append(fmt.format(expires)).append(" GMT");
        }

        if (maxAge != null){
            s.append("; Max-Age=").append(maxAge);
        }

        if (domain != null){
            s.append("; Domain=").append(domain);
        }

        if (path != null){
            s.append("; Path=").append(path);
        }

        if (secure){
            s.append("; Secure");
        }

        if (httpOnly){
            s.append("; HttpOnly");
        }

        if (sameSite != null){
            s.append("; SameSite=").append(sameSite);
        }

        return s.toString();
    }
}
