
package util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import model.annotation.Positive;
import model.annotation.MaxLength;

public class AnnotationValidator {

    public static String[] validate(Object obj) {

        List<String> errors = new ArrayList<>();

        Class<?> current = obj.getClass();

        while (current != null) {

            Field[] fields = current.getDeclaredFields();

            for (Field field : fields) {
                try {
                    field.setAccessible(true);
                    Object value = field.get(obj);

                    if (field.isAnnotationPresent(Positive.class)) {
                        if (value instanceof Number
                                && ((Number) value).longValue() <= 0) {
                            errors.add(field.getName() + ": "
                                    + field.getAnnotation(Positive.class).message());
                        }
                    }

                    if (field.isAnnotationPresent(MaxLength.class)) {
                        if (value instanceof String
                                && ((String) value).length()
                                > field.getAnnotation(MaxLength.class).value()) {
                            errors.add(field.getName()
                                    + ": maximum length exceeded");
                        }
                    }

                } catch (IllegalAccessException e) {
                    errors.add("Cannot access field: " + field.getName());
                }
            }

            current = current.getSuperclass();
        }

        return errors.toArray(new String[0]);
    }
}
