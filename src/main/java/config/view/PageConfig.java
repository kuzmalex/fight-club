package config.view;

import infostructure.di.annotations.Configuration;
import infostructure.di.annotations.ManagedCollectionElement;
import infostructure.di.annotations.Value;
import server.view.html.Page;

@Configuration
public class PageConfig {

    @ManagedCollectionElement(name = "pages")
    public Page loginPage(
            @Value("login_page_component") String name,
            @Value("login_page_html") String htmlLocation,
            @Value("login_page_css") String cssLocation
    ){
        return new Page(name, htmlLocation, cssLocation);
    }

    @ManagedCollectionElement(name = "pages")
    public Page startPage(
            @Value("start_page_component") String name,
            @Value("start_page_html") String htmlLocation,
            @Value("start_page_css") String cssLocation
    ){
        return new Page(name, htmlLocation, cssLocation);
    }

    @ManagedCollectionElement(name = "pages")
    public Page matchingPage(
            @Value("matching_page_component") String name,
            @Value("matching_page_html") String htmlLocation,
            @Value("matching_page_css") String cssLocation
    ){
        return new Page(name, htmlLocation, cssLocation);
    }

    @ManagedCollectionElement(name = "pages")
    public Page timerPage(
            @Value("timer_page_component") String name,
            @Value("timer_page_html") String htmlLocation,
            @Value("timer_page_css") String cssLocation
    ){
        return new Page(name, htmlLocation, cssLocation);
    }

    @ManagedCollectionElement(name = "pages")
    public Page duelPage(
            @Value("duel_page_component") String name,
            @Value("duel_page_html") String htmlLocation,
            @Value("duel_page_css") String cssLocation
    ){
        return new Page(name, htmlLocation, cssLocation);
    }
}
