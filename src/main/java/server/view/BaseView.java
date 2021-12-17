package server.view;

import server.view.html.*;
import server.service.ComponentService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseView<M extends PageModel> implements View<M> {

    private final Component format;
    private final ComponentService componentService;
    private final PageFactory pageFactory;

    public BaseView(Component format, ComponentService componentService, PageFactory pageFactory) {
        this.format = format;
        this.componentService = componentService;
        this.pageFactory = pageFactory;
    }

    @Override
    public String getPage(M model) {
        return toPage(format, model);
    }

    public String toPage(Component component, PageModel model){

        Map<String, String> values = model.getValues();
        Map<String, List<String>> collectionValues = model.getCollectionValues();

        Map<String, String> arguments = new HashMap<>();

        for (ComponentVariable variable : component.variables()){
            if (variable.type().equals(VariableType.String)){
                arguments.put(variable.name(), values.get(variable.name()));
            }
            if (variable.type().equals(VariableType.Component)){
                Component arg = componentService.getComponent(variable.name());
                arguments.put(variable.name(), toPage(arg, model));
            }
            else if (variable.type().equals(VariableType.ListOfComponents)){
                Map<String, List<String>> requiredMultipleValues = new HashMap<>();
                Component c = componentService.getComponent(variable.name());
                int componentListSize = 0;
                for (ComponentVariable var : c.variables()){
                    List<String> requiredList = collectionValues.get(var.name());
                    requiredMultipleValues.put(var.name(), requiredList);
                    if (componentListSize == 0) componentListSize = requiredList.size();
                    if (componentListSize != requiredList.size())
                        throw new RuntimeException("Multiple values size doesn't match");
                }
                StringBuilder listComponentBuilder = new StringBuilder();
                for (int i = 0; i < componentListSize; i++){
                    Map<String, String> valuesCopy = new HashMap<>(values);

                    for (Map.Entry<String, List<String>> entry : requiredMultipleValues.entrySet()){
                        String name = entry.getKey();
                        List<String> requiredValues = entry.getValue();
                        valuesCopy.put(name, requiredValues.get(i));
                    }

                    listComponentBuilder.append(
                            toPage(c, new PageModel() {
                                @Override
                                public Map<String, String> getValues() {
                                    return valuesCopy;
                                }

                                @Override
                                public Map<String, List<String>> getCollectionValues() {
                                    return collectionValues;
                                }
                            })
                    );
                }
                arguments.put(variable.name(), listComponentBuilder.toString());
            }
        }

        return pageFactory.from(component, arguments);
    }
}
