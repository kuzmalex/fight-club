package server.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ImageServiceImpl implements ImageService {

    Map<String, byte[]> imageCache = new ConcurrentHashMap<>();
    String imageLocation = "images/";

    @Override
    public byte[] getImage(String imageName) {
        return imageCache.computeIfAbsent(imageName, this::loadImage);
    }

    private byte[] loadImage(String imageName){
        Path imagePath = getPath(imageName);

        try {
            return Files.readAllBytes(imagePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Path getPath(String imageName){
        return Paths.get(imageLocation + imageName);
    }
}
