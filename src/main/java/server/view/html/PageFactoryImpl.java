package server.view.html;

import java.util.Map;

public class PageFactoryImpl implements PageFactory {

    public String from(Component format, Map<String, String> arguments){

        if (arguments.size() != format.variables().size()){
            throw new RuntimeException("Invalid arguments number");
        }

        StringBuilder content = new StringBuilder(format.pageParts().get(0));
        for (int i = 0; i < format.variables().size(); i++){
            ComponentVariable variable = format.variables().get(i);
            String arg = arguments.get(variable.name());
            if (arg != null){
                content.append(arg);
            }
            content.append(format.pageParts().get(i + 1));
        }
        return content.toString();
    }
}
