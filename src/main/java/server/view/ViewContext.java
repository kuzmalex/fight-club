package server.view;

import server.view.condition.ViewCondition;
import server.view.factory.ViewModelFactory;
import server.view.html.PageModel;

public record ViewContext<M extends PageModel>(
        ViewCondition viewCondition,
        View<M> view,
        ViewModelFactory<M> modelFactory
){}
