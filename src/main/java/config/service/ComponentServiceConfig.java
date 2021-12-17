package config.service;

import infostructure.di.annotations.Configuration;
import infostructure.di.annotations.ManagedObject;
import infostructure.di.annotations.Named;
import infostructure.di.annotations.Value;
import server.view.html.ComponentParser;
import server.view.html.Page;
import server.service.ComponentService;
import server.service.ComponentServiceImpl;

import java.util.Collection;

@Configuration
public class ComponentServiceConfig {

    @ManagedObject
    public ComponentService componentService(
            @Named("pages")Collection<Page> pages,
            ComponentParser parser,
            @Value("components_location")String componentsLocation
    ){
        return new ComponentServiceImpl(parser, pages, componentsLocation);
    }
}
