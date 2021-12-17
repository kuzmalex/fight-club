package dao.mapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

public class ResultSetIterator implements Iterator<Map<String, Object>>{

    private final ResultSet resultSet;
    private final List<String> columns;

    private boolean next = false;

    public ResultSetIterator(ResultSet resultSet) {
        this.resultSet = resultSet;
        columns = getColumns(resultSet);
    }

    @Override
    public boolean hasNext() {
        if (next) return true;

        try {
            if (resultSet.next()){
                return next = true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    @Override
    public Map<String, Object> next() {
        try {
            if (hasNext()) {
                var row = new HashMap<String, Object>(columns.size());
                for (int i = 0; i < columns.size(); i++) {
                    row.put(columns.get(i), resultSet.getObject(i+1));
                }
                next=false;
                return row;
            }
            throw new NoSuchElementException();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> getColumns(ResultSet resultSet){
        var columns = new ArrayList<String>();

        try {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++){
                columns.add(metaData.getColumnName(i));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return columns;
    }
}
