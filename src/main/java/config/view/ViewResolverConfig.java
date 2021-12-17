package config.view;

import infostructure.di.annotations.Configuration;
import infostructure.di.annotations.ManagedObject;
import infostructure.di.annotations.Named;
import server.view.ViewContext;
import server.view.ViewResolver;
import server.view.ViewResolverImpl;
import server.view.html.PageModel;

import java.util.Collection;

@Configuration
public class ViewResolverConfig {

    @ManagedObject
    public ViewResolver viewResolver(@Named("contexts") Collection<ViewContext<? extends PageModel>> contexts){
        return new ViewResolverImpl(contexts);
    }
}
