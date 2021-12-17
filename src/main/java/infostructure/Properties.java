package infostructure;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class Properties {
    private final Map<String, String> properties = new HashMap<>();

    public Properties(Path propertiesFile){
        try (BufferedReader reader = new BufferedReader(new FileReader(propertiesFile.toFile()))){
            String record;
            while ((record = reader.readLine()) != null){
                readRecord(record);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void readRecord(String line){
        if (line.length() > 0){
            String[] words = line.split("=");
            if (words.length == 2){
                String key = words[0];
                String value = words[1];
                properties.put(key, value);
            }
        }
    }

    public String getValue(String key){
        return properties.get(key);
    }
}
