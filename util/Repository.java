
package util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import model.annotation.Id;

public class Repository<T> {

    private HashMap<String, T> data = new HashMap<>();

    public T save(T entity) {
        String id = getId(entity);
        data.put(id, entity);
        return entity;
    }

    public T findById(String id) {
        return data.get(id);
    }

    public ArrayList<T> findAll() {
        return new ArrayList<>(data.values());
    }

    public boolean delete(String id) {
        return data.remove(id) != null;
    }

    @SafeVarargs
    public final void saveAll(T... items) {
        for (T item : items) {
            save(item);
        }
    }

    private String getId(T entity) {
        Class<?> type = entity.getClass();

        while (type != null) {
            for (Field field : type.getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class)) {
                    try {
                        field.setAccessible(true);
                        return field.get(entity).toString();
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            type = type.getSuperclass();
        }

        throw new IllegalArgumentException("@Id field not found");
    }

    public static <N extends Number> double average(N[] values) {
        if (values.length == 0) {
            return 0;
        }

        double total = 0;

        for (N value : values) {
            total += value.doubleValue();
        }

        return total / values.length;
    }
}
