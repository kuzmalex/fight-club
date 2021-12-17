package server.view.model.page;

import server.view.html.PageModel;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimerViewModel implements PageModel {
    private final Map<String, String> singleValues = new HashMap<>();

    public TimerViewModel(int secondsBeforeDuelStart) {
        singleValues.put("timer", String.valueOf(secondsBeforeDuelStart));
    }

    @Override
    public Map<String, String> getValues() {
        return singleValues;
    }

    @Override
    public Map<String, List<String>> getCollectionValues() {
        return Collections.EMPTY_MAP;
    }
}
