package server.handlers.request;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import server.service.ImageService;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ImageRequestHandler implements HttpHandler {

    private final int BUFFER_SIZE = 4096;

    private final ImageService imageService;

    public ImageRequestHandler(ImageService imageService) {
        this.imageService = imageService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String[] pathParts = exchange.getRequestURI().getPath().split("/");

        if (pathParts.length > 1) {

            String imageName = pathParts[pathParts.length - 1];
            byte[] image = imageService.getImage(imageName);
            exchange.sendResponseHeaders(200, image.length);

            try (BufferedOutputStream out = new BufferedOutputStream(exchange.getResponseBody())) {
                try (ByteArrayInputStream bis = new ByteArrayInputStream(image)) {
                    byte [] buffer = new byte [BUFFER_SIZE];
                    int count ;
                    while ((count = bis.read(buffer)) != -1) {
                        out.write(buffer, 0, count);
                    }
                    out.close();
                }
            }
        }
        else {
            exchange.sendResponseHeaders(404, -1);
        }
    }
}
