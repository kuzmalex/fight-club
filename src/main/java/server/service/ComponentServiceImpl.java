package server.service;

import server.view.html.Component;
import server.view.html.ComponentParser;
import server.view.html.Page;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class ComponentServiceImpl implements ComponentService {

    private final ComponentParser parser;
    private final ConcurrentHashMap<String, Component> loadedComponents = new ConcurrentHashMap<>();
    private final String componentsLocation;

    public ComponentServiceImpl(ComponentParser parser, Collection<Page> pages, String componentsLocation) {
        this.componentsLocation = componentsLocation;
        this.parser = parser;
        for (Page page : pages){
            Path htmlPath = Paths.get(page.htmlLocation());
            Path cssPath = Paths.get(page.cssLocation());
            loadedComponents.put(
                    page.name(),
                    parser.parse(htmlPath, cssPath)
            );
        }
    }

    @Override
    public Component getComponent(String name) {
        Objects.requireNonNull(name);
        return loadedComponents.computeIfAbsent(
                name,
                n -> {
                    Path componentPath = Paths.get(componentsLocation + name + ".component");
                    return parser.parse(componentPath);
                });
    }
}
