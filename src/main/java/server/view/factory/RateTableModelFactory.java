package server.view.factory;

import dao.UserDao;
import domain.User;
import server.RequestContext;
import server.view.model.dto.RateTableModel;

import java.util.LinkedHashMap;
import java.util.List;

public class RateTableModelFactory implements ViewModelFactory<RateTableModel>{

    private final UserDao userDao;
    private final RequestContext requestContext;
    private final int usersPerPage;

    public RateTableModelFactory(UserDao userDao, RequestContext requestContext, int usersPerPage) {
        this.userDao = userDao;
        this.requestContext = requestContext;
        this.usersPerPage = usersPerPage;
    }

    @Override
    public RateTableModel create(User user) {

        int totalPageNumber = userDao.getUsersNumber() / usersPerPage;
        if (userDao.getUsersNumber() % usersPerPage > 0) totalPageNumber++;

        int selectedPage =
                Integer.parseInt(
                        requestContext.getRequestParams()
                                .getOrDefault("selected_leader_board_page", "1")
                );

        if (selectedPage < totalPageNumber || selectedPage <= 0){
            selectedPage = 1;
        }

        int offset = usersPerPage * (selectedPage - 1);
        List<User> users = userDao.getUserSortedByRate(offset, usersPerPage);

        var rateByUserName = new LinkedHashMap<String, Integer>();
        for (User u : users){
            rateByUserName.put(u.getName(), u.getRate());
        }

        int firstPaginationNumber = Math.max(1, selectedPage - 3);
        int lastPaginationNumber = Math.min(totalPageNumber, selectedPage + 3);

        if (lastPaginationNumber < selectedPage + 3){
            firstPaginationNumber -= selectedPage + 3 - lastPaginationNumber;
            firstPaginationNumber = Math.max(1, firstPaginationNumber);
        }
        if (firstPaginationNumber > selectedPage - 3){
            lastPaginationNumber += firstPaginationNumber - selectedPage + 3;
            lastPaginationNumber = Math.min(totalPageNumber, lastPaginationNumber);
        }

        var paginationOptions = new LinkedHashMap<String, Integer>();

        if (selectedPage != firstPaginationNumber && totalPageNumber > 0){
            paginationOptions.put("Previous", selectedPage-1);
        }

        for (int i = firstPaginationNumber; i <= lastPaginationNumber; i++){
            paginationOptions.put(String.valueOf(i), i);
        }

        if(selectedPage != lastPaginationNumber && totalPageNumber > 0){
            paginationOptions.put("Next", selectedPage+1);
        }

        return new RateTableModel(rateByUserName, paginationOptions, selectedPage);
    }
}
