package config.view;

import infostructure.di.annotations.Configuration;
import infostructure.di.annotations.ManagedObject;
import server.RequestContext;
import server.view.html.MetricMeasurePageFactory;
import server.view.html.PageFactory;
import server.view.html.PageFactoryImpl;

@Configuration
public class PageFactoryConfig {

    @ManagedObject
    public PageFactory pageFactory(RequestContext requestContext){
        return new MetricMeasurePageFactory(new PageFactoryImpl(), requestContext);
    }
}
