package dao.mapper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ResultSetMapperImpl implements ResultSetMapper {

    private final Map<Class<?>, Map<String, Field>> fieldsByClass = new ConcurrentHashMap<>();

    public <T> Optional<T> map(ResultSet resultSet, Class<T> clazz){

        var iterator = new ResultSetIterator(resultSet);
        T entity = null;
        if (iterator.hasNext()){
            entity = map(iterator.next(), clazz);
        }

        return Optional.ofNullable(entity);
    }

    public <T> List<T> mapCollection(ResultSet resultSet, Class<T> clazz){

        var iterator = new ResultSetIterator(resultSet);
        List<T> entities = new ArrayList<>();
        while (iterator.hasNext()){
            entities.add(
                    map(iterator.next(), clazz)
            );
        }

        return entities;
    }

    public <T> T map(Map<String, Object> row, Class<T> clazz){
        try {
            T entity = clazz.getConstructor().newInstance();
            row.forEach(
                    (column, value) -> {
                        Field field = getFieldsByClass(clazz).get(column);
                        if (field != null){
                            try {
                                field.set(entity, value);
                            } catch (IllegalAccessException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
            );
            return entity;

        } catch (InstantiationException | IllegalAccessException |
                InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, Field> getFieldsByClass(Class<?> clazz){

        return fieldsByClass.computeIfAbsent(clazz,
                cls -> {
                    var fields = new HashMap<String, Field>();
                    for (Field field : cls.getDeclaredFields()){
                        Column column = field.getAnnotation(Column.class);
                        if (column != null){
                            field.setAccessible(true);
                            fields.put(column.name(), field);
                        }
                    }
                    return fields;
                }
        );
    }
}
