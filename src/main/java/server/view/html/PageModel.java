package server.view.html;

import server.view.Model;

import java.util.List;
import java.util.Map;

public interface PageModel extends Model {
    Map<String, String> getValues();
    Map<String, List<String>> getCollectionValues();
}
