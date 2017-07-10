package ru.otus.lesson13.webserver;

import ru.otus.lesson13.webserver.annotations.WebModified;
import ru.otus.lesson13.webserver.annotations.WebSettings;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * Created by piphonom
 */
final class WebToObjectMapper {
    public static void map(HttpServletRequest request, Object object) {
        Class<?> clazz = object.getClass();
        WebSettings settingsAnno = clazz.getAnnotation(WebSettings.class);
        if(settingsAnno == null)
            return;
        Field fields[] = clazz.getDeclaredFields();
        Arrays.stream(fields).forEach(field ->{
            WebModified modifiedAnno = field.getAnnotation(WebModified.class);
            if (modifiedAnno != null) {
                field.setAccessible(true);
                String value = request.getParameter(modifiedAnno.name());
                if (value != null) {
                    try {
                        switch(field.getType().getSimpleName()) {
                            case "Integer":
                                field.set(object, new Integer(value));
                                break;
                            case "Long":
                                field.set(object, new Long(value));
                                break;
                            case "String":
                                field.set(object, value);
                                break;
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        return;
                    }
                    field.setAccessible(false);
                }
            }
        });
    }
}
