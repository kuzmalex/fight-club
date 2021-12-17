package server.view.model.page;

import server.view.html.PageModel;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MatchWaitingModel implements PageModel {

    @Override
    public Map<String, String> getValues() {
        return Collections.EMPTY_MAP;
    }

    @Override
    public Map<String, List<String>> getCollectionValues() {
        return Collections.EMPTY_MAP;
    }
}
