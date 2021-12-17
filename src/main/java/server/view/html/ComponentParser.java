package server.view.html;

import java.nio.file.Path;

public interface ComponentParser {
    Component parse(Path html);
    Component parse(Path html, Path css);
}
