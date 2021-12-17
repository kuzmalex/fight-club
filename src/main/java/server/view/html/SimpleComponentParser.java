package server.view.html;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class SimpleComponentParser implements ComponentParser {

    public Component parse(Path html, Path css){

        PageProcessor pageProcessor;
        try (BufferedReader reader = new BufferedReader(new FileReader(html.toFile()))){

            String cssContent = null;
            if (css != null) {
                cssContent = new String(Files.readAllBytes(css));
            }

            pageProcessor = new PageProcessor(html.getFileName().toString(), cssContent);
            String line;
            while ((line = reader.readLine()) != null){
                pageProcessor.processLine(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return pageProcessor.getFormat();
    }

    public Component parse(Path html){
        return parse(html, null);
    }

    private static class PageProcessor {

        private final List<String> pageParts = new ArrayList<>();
        private final List<ComponentVariable> variables = new ArrayList<>();
        private StringBuilder currentPart = new StringBuilder();

        String cssContent;
        String name;

        PageProcessor(String name, String cssContent){
            this.cssContent = cssContent;
            this.name = name;
        }

        void processLine(String line){
            if (cssContent != null) {
                line = styleInsertion(line, cssContent);
            }
            variableExtraction(line);
        }

        Component getFormat(){
            pageParts.add(currentPart.toString());
            return new Component(name, pageParts, variables);
        }

        private void variableExtraction(String line){
            String[] split = line.split("\\$");

            for (int i = 1; i <= split.length; i++){
                if (i % 2 == 0){
                    String variable = split[i-1];
                    int nameStart = variable.indexOf("{");
                    int nameEnd = variable.lastIndexOf("}");
                    String name = variable.substring(nameStart+1, nameEnd);

                    String typeRecord = "S";
                    if (nameStart > 0){
                        typeRecord = variable.substring(0, nameStart);
                    }

                    VariableType type = switch (typeRecord) {
                        case ("C") -> VariableType.Component;
                        case ("L") -> VariableType.ListOfComponents;
                        case ("S") -> VariableType.String;
                        default -> VariableType.String;
                    };

                    variables.add(new ComponentVariable(name, type));

                    if (!currentPart.toString().isBlank()) {
                        pageParts.add(currentPart.toString());
                    }
                    currentPart = new StringBuilder();
                }
                else {
                    currentPart.append(split[i - 1]);
                }
            }
        }

        private String styleInsertion(String line, String cssContent){
            String[] split = line.split("<head>");
            if (split.length == 2){
                line = split[0] + "<head><style>" + cssContent + "</style>" + split[1];
            }
            if (split.length == 0){
                line = "<head><style>" + cssContent + "</style>";
            }
            return line;
        }
    }
}
