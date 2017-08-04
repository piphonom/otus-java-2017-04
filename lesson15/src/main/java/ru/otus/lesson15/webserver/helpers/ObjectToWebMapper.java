package ru.otus.lesson15.webserver.helpers;

import ru.otus.lesson15.webserver.annotations.WebAccessed;
import ru.otus.lesson15.webserver.annotations.WebModified;
import ru.otus.lesson15.webserver.annotations.WebSettings;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * Created by piphonom
 */
public final class ObjectToWebMapper {

    private static String HTML_HEADER_TEMPLATE =
            "<!DOCTYPE html>\n" +
            "<html>\n";// +
            //"<body onload='setInterval(function(){refresh()}, 1000 ); refresh();>";

    private static String HTML_FOOTER_TEMPLATE =
            "</html>\n";

    private static String HEAD_HEADER_TEMPLATE = "<head>\n";

    private static String HEAD_FOOTER_TEMPLATE = "</head>\n";

    private static String BODY_HEADER_TEMPLATE = "<body onload='setInterval(function(){refresh()}, 1000 ); refresh();'>\n";

    private static String BODY_FOOTER_TEMPLATE = "</body>\n";

    private static String FORM_START_TEMPLATE =
            "<form action=\"/settings\" method=\"post\">\n";

    private static String FORM_END_TEMPLATE =
            "    <button type=\"submit\">Update</button>\n" +
            "    </div>\n" +
            "</form>\n";

    private final StringBuilder webBuilder = new StringBuilder();

    public ObjectToWebMapper(Object object) {
        webBuilder.append(HTML_HEADER_TEMPLATE)
                .append(HEAD_HEADER_TEMPLATE);
        createAjax(object);
        webBuilder.append(HEAD_FOOTER_TEMPLATE)
                .append(BODY_HEADER_TEMPLATE);
        createForm(object);
        webBuilder.append(BODY_FOOTER_TEMPLATE)
                .append(HTML_FOOTER_TEMPLATE);
    }

    private void createForm(Object object) {
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
                webBuilder.append("</div>\n");
            } else {
                WebAccessed accessedAnno = field.getAnnotation(WebAccessed.class);
                if (accessedAnno != null) {
                    webBuilder.append("<div>\n<p><label><b>").append(accessedAnno.name()).append("</b></label>: <span id='").append(field.getName()).append("'></span></p>\n");
                    webBuilder.append("</div>\n");
                }
            }
        });
        webBuilder.append(FORM_END_TEMPLATE);
    }

    private final String JS_HTML_HEADER = "<script>\n";
    private final String JS_HTML_FOOTER = "</script>\n";

    private final String JS_FUNCTION_HEADER = "function updateObject(){\n" +
                                    "var xhttp = new XMLHttpRequest();\n" +
                                    "xhttp.onreadystatechange = function() {\n" +
                                    "if (this.readyState === XMLHttpRequest.DONE && this.status == 200) {\n";

    private final String JS_FUNCTION_FOOTER = "} else {\n" +
                                              "alert(\"Server response: \" + this.status);\n" +
                                              "}\n" +
                                              "}\n;" +
                                              "xhttp.open(\"GET\", \"/settingssync\", true);\n" +
                                              "xhttp.send();\n" +
                                              "}\n" +
                                              "function refresh() {\n" +
                                              "updateObject();\n" +
                                              "}\n";

    private void createAjax(Object object) {
        webBuilder.append(JS_HTML_HEADER).append(JS_FUNCTION_HEADER);
        Class<?> clazz = object.getClass();
        Field fields[] = clazz.getDeclaredFields();
        Arrays.stream(fields).forEach(field -> {
            WebAccessed accessedAnno = field.getAnnotation(WebAccessed.class);
            if (accessedAnno != null) {
                webBuilder.append("document.getElementById('")
                        .append(field.getName())
                        .append("').innerHTML = JSON.parse(this.responseText).")
                        .append(field.getName())
                        .append(";\n");
            }
        });
        webBuilder.append(JS_FUNCTION_FOOTER).append(JS_HTML_FOOTER);
    }

    @Override
    public String toString() {
        return webBuilder.toString();
    }
}
