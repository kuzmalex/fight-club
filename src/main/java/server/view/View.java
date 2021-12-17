package server.view;

import server.view.html.PageModel;

public interface View<M extends PageModel> {
    String getPage(M model);
}
