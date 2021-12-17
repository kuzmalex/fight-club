package server.view.html;

import java.util.List;

public record Component(String name, List<String> pageParts, List<ComponentVariable> variables) {
}
