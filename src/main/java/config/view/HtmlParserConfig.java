package config.view;

import infostructure.di.annotations.Configuration;
import infostructure.di.annotations.ManagedObject;
import server.view.html.ComponentParser;
import server.view.html.SimpleComponentParser;

@Configuration
public class HtmlParserConfig {

    @ManagedObject
    public ComponentParser htmlParser(){
        return new SimpleComponentParser();
    }
}
