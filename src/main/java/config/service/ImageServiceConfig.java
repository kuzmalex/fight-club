package config.service;

import infostructure.di.annotations.Configuration;
import infostructure.di.annotations.ManagedObject;
import server.service.ImageService;
import server.service.ImageServiceImpl;

@Configuration
public class ImageServiceConfig {
    @ManagedObject
    public ImageService imageService(){
        return new ImageServiceImpl();
    }
}
