package dao.mapper;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ResultSetMapper {
    <T> T map(Map<String, Object> row, Class<T> clazz);
    <T> Optional<T> map(ResultSet resultSet, Class<T> clazz);
    <T> List<T> mapCollection(ResultSet resultSet, Class<T> clazz);
}
