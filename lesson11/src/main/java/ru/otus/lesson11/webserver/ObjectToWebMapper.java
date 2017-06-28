package ru.otus.lesson11.webserver;

import ru.otus.lesson11.webserver.annotations.WebAccessed;
import ru.otus.lesson11.webserver.annotations.WebModified;
import ru.otus.lesson11.webserver.annotations.WebSettings;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * Created by piphonom
 */
class ObjectToWebMapper {

    private static String PAGE_HEADER_TEMPLATE =
            "<!DOCTYPE html>" +
            "<html>" +
            "<body>";

    private static String FORM_START_TEMPLATE =
            "<form action=\"/settings\" method=\"post\">";

    private static String FORM_END_TEMPLATE =
            "    <button type=\"submit\">Update</button>" +
            "    </div>" +
            "</form>";

    private static String PAGE_FOOTER_TEMPLATE =
            "</body>" +
            "</html>";

    private final StringBuilder webBuilder = new StringBuilder();

    public ObjectToWebMapper(Object object) {
        webBuilder.append(PAGE_HEADER_TEMPLATE);
        parseObject(object);
        webBuilder.append(PAGE_FOOTER_TEMPLATE);
    }

    private void parseObject(Object object) {
        Class<?> clazz = object.getClass();
        WebSettings settingsAnno = clazz.getAnnotation(WebSettings.class);
        if(settingsAnno == null)
            return;
        webBuilder.append("<h2>").append(settingsAnno.name()).append("</h2>");
        webBuilder.append(FORM_START_TEMPLATE);
        Field fields[] = clazz.getDeclaredFields();
        Arrays.stream(fields).forEach(field -> {
            WebModified modifiedAnno = field.getAnnotation(WebModified.class);
            if (modifiedAnno != null) {
                field.setAccessible(true);
                Object value;
                try {
                    value = field.get(object);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    return;
                }
                field.setAccessible(false);
                webBuilder.append("<div><label><b>").append(modifiedAnno.name()).append("</b></label>: ");
                webBuilder.append("<input type=\"text\" name=\"").append(modifiedAnno.name()).append("\" value=\"").
                        append(value).append("\">");
                webBuilder.append("</div>");
            } else {
                WebAccessed accessedAnno = field.getAnnotation(WebAccessed.class);
                if (accessedAnno != null) {
                    field.setAccessible(true);
                    Object value;
                    try {
                        value = field.get(object);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        return;
                    }
                    field.setAccessible(false);
                    webBuilder.append("<div><label><b>").append(accessedAnno.name()).append("</b></label>: ");
                    webBuilder.append(value);
                    webBuilder.append("</div>");
                }
            }
        });
        webBuilder.append(FORM_END_TEMPLATE);
    }

    @Override
    public String toString() {
        return webBuilder.toString();
    }
}
