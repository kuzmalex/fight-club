package server.view.html;

import java.util.Map;

public interface PageFactory {
    String from(Component format, Map<String, String> arguments);
}
