package server.view.model.page;

import server.view.html.PageModel;
import server.view.model.dto.UnitViewModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DuelViewModel implements PageModel {

    private final Map<String, String> singleValues = new HashMap<>();
    private final Map<String, List<String>> collectionValues = new HashMap<>();

    public DuelViewModel(
            UnitViewModel allyUnit,
            UnitViewModel enemyUnit,
            List<String> log,
            boolean hitAvailable
    ){
        singleValues.put("allyName", allyUnit.name());
        singleValues.put("allyHealth", String.valueOf(allyUnit.health()));
        singleValues.put("allyMaxHealth", String.valueOf(allyUnit.maxHealth()));
        singleValues.put("allyStrength", String.valueOf(allyUnit.strength()));

        int allyHealthPercent = (int)(allyUnit.health() * 100 / allyUnit.maxHealth());
        singleValues.put("allyHealthPercent", String.valueOf(allyHealthPercent));

        singleValues.put("enemyName", enemyUnit.name());
        singleValues.put("enemyHealth", String.valueOf(enemyUnit.health()));
        singleValues.put("enemyMaxHealth", String.valueOf(enemyUnit.maxHealth()));
        singleValues.put("enemyStrength", String.valueOf(enemyUnit.strength()));

        int enemyHealthPercent = (int)(enemyUnit.health() * 100 / enemyUnit.maxHealth());
        singleValues.put("enemyHealthPercent", String.valueOf(enemyHealthPercent));

        singleValues.put("hitAvailable", hitAvailable ? "" : "disabled");

        collectionValues.put("log", log);
    }

    @Override
    public Map<String, String> getValues() {
        return singleValues;
    }

    @Override
    public Map<String, List<String>> getCollectionValues() {
        return collectionValues;
    }
}
