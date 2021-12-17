package server.view.model.page;

import server.view.html.PageModel;
import server.view.model.dto.RateTableModel;

import java.util.*;

public class StartViewModel implements PageModel {

    private final Map<String, String> singleValues = new HashMap<>();
    private final Map<String, List<String>> collectionValues = new HashMap<>();

    public StartViewModel(int userRate, RateTableModel rateTableModel) {

        singleValues.put("userRate", String.valueOf(userRate));
        singleValues.put("selectedListPage", String.valueOf(rateTableModel.selectedPage()));

        var usersByRate = rateTableModel.rateByUsers();
        List<String> users = new ArrayList<>(usersByRate.size());
        List<String> rates = new ArrayList<>(usersByRate.size());
        usersByRate.forEach(
                (u, r) -> {
                    users.add(u);
                    rates.add(String.valueOf(r));
                }
        );
        collectionValues.put("user", users);
        collectionValues.put("rate", rates);

        var paginationOptions = rateTableModel.paginationOptions();
        List<String> paginationNumbers = new ArrayList<>(paginationOptions.size());
        List<String> paginationNames = new ArrayList<>(paginationOptions.size());
        paginationOptions.forEach(
                (name, number) -> {
                    paginationNumbers.add(String.valueOf(number));
                    paginationNames.add(name);
                }
        );
        collectionValues.put("paginationPageNumber", paginationNumbers);
        collectionValues.put("paginationOptionName", paginationNames);
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
