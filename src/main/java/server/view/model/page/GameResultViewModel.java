package server.view.model.page;

import server.view.html.PageModel;

import java.util.List;
import java.util.Map;

public class GameResultViewModel implements PageModel {

    private final StartViewModel startViewModel;

    public GameResultViewModel(StartViewModel startViewModel, String gameResultRecord) {
        this.startViewModel = startViewModel;

        startViewModel.getValues().put("lastGameResult", gameResultRecord);
    }

    @Override
    public Map<String, String> getValues() {
        return startViewModel.getValues();
    }

    @Override
    public Map<String, List<String>> getCollectionValues() {
        return startViewModel.getCollectionValues();
    }
}
