package server.view;

import domain.User;
import server.view.html.PageModel;

import java.util.Collection;

public class ViewResolverImpl implements ViewResolver {

    private final Collection<ViewContext<? extends PageModel>> contexts;

    public ViewResolverImpl(Collection<ViewContext<? extends PageModel>> contexts) {
        this.contexts = contexts;
    }

    public String getPage(User user){
        for (ViewContext<? extends PageModel> viewContext : contexts){
            if (viewContext.viewCondition().isSatisfied(user)){
                return getPage(viewContext, user);
            }
        }
        return null;
    }

    private <M extends PageModel> String getPage(ViewContext<M> viewContext, User user){
        M model = viewContext.modelFactory().create(user);
        return viewContext.view().getPage(model);
    }
}
