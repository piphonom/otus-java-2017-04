package ru.otus.lesson13.myorm;

/**
 * Created by piphonom
 */

import ru.otus.lesson13.base.datasets.DataSet;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SQLConstructor {

    private static <T extends DataSet> String constructSetSubrequest(T dataSet) {
        Class<?> clazz = dataSet.getClass();
        StringBuilder setBuilder = new StringBuilder();
        setBuilder.append(" SET ");
        Field[] fields = clazz.getDeclaredFields();
        Arrays.stream(fields).forEach(field -> {
            if(field.getAnnotation(Id.class) == null) {
                field.setAccessible(true);
                try {
                    StringBuilder tmpBuilder = new StringBuilder();
                    Column columnAnno = field.getAnnotation(Column.class);
                    if(columnAnno != null) {
                        tmpBuilder.append(columnAnno.name().equals("") ? field.getName() : columnAnno.name());
                        tmpBuilder.append("=\'").append(field.get(dataSet)).append("\',");
                        setBuilder.append(tmpBuilder);
                    }
                    field.setAccessible(false);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
        setBuilder.append(";");
        return setBuilder.toString().replace(",;", "");
    }

    private static String constructWhereIdSubrequest(int idValue, Class<? extends DataSet> clazz) {
        StringBuilder whereBuilder = new StringBuilder();
        List<Field> ids = Arrays.stream(clazz.getSuperclass().getDeclaredFields())
                .filter(field -> field.getAnnotation(Id.class) != null)
                .collect(Collectors.toList());
        if (ids.size() != 1) {
            return null;
        }
        String idColumnName = ids.get(0).getAnnotation(Column.class).name();
        if (idColumnName.equals(""))
            idColumnName = ids.get(0).getName();
        whereBuilder.append(" WHERE ").append(idColumnName).append("=").append(idValue);
        return whereBuilder.toString();
    }

    private static String constructWhereNameSubrequest(String name, Class<? extends DataSet> clazz) {
        StringBuilder whereBuilder = new StringBuilder();
        List<Field> names = Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.getAnnotation(Column.class) != null && field.getAnnotation(Column.class).name().equals("name"))
                .collect(Collectors.toList());
        if (names.size() != 1) {
            return null;
        }
        whereBuilder.append(" WHERE ").append("name").append("='").append(name).append("'");
        return whereBuilder.toString();
    }

    private static String constructSimpleSelect(Class<? extends DataSet> clazz) {
        StringBuilder selectBuilder = new StringBuilder();
        selectBuilder.append("SELECT * FROM ");
        Table tableAnno = clazz.getAnnotation(Table.class);
        if (tableAnno == null) {
            selectBuilder.append(clazz.getSimpleName());
        } else {
            selectBuilder.append(tableAnno.name());
        }
        return selectBuilder.toString();
    }

    public static <T extends DataSet> String constructSimpleInsert(T dataSet) {
        Class<?> clazz = dataSet.getClass();
        StringBuilder updateBuilder = new StringBuilder();
        updateBuilder.append("INSERT INTO ");
        Table tableAnno = clazz.getAnnotation(Table.class);
        if (tableAnno == null) {
            updateBuilder.append(clazz.getSimpleName());
        } else {
            updateBuilder.append(tableAnno.name());
        }
        updateBuilder.append(constructSetSubrequest(dataSet))
                     .append(";");

        return updateBuilder.toString();
    }

    public static <T extends DataSet> String constructUpdateById(int idValue, T dataSet) {
        Class<?> clazz = dataSet.getClass();
        StringBuilder updateBuilder = new StringBuilder();
        updateBuilder.append("UPDATE ");
        Table tableAnno = clazz.getAnnotation(Table.class);
        if (tableAnno == null) {
            updateBuilder.append(clazz.getSimpleName());
        } else {
            updateBuilder.append(tableAnno.name());
        }
        updateBuilder.append(constructSetSubrequest(dataSet))
                     .append(constructWhereIdSubrequest(idValue, dataSet.getClass()))
                     .append(";");

        return updateBuilder.toString();
    }

    public static String constructSelectById(int idValue, Class<? extends DataSet> clazz) {
        StringBuilder selectBuilder = new StringBuilder(constructSimpleSelect(clazz));
        selectBuilder.append(constructWhereIdSubrequest(idValue, clazz)).append(";");
        return selectBuilder.toString();
    }

    public static String constructSelectByName(String name, Class<? extends DataSet> clazz) {
        StringBuilder selectBuilder = new StringBuilder(constructSimpleSelect(clazz));
        selectBuilder.append(constructWhereNameSubrequest(name, clazz)).append(";");
        return selectBuilder.toString();
    }

    public static String constructSelectAll(Class<? extends DataSet> clazz) {
        return new StringBuilder(constructSimpleSelect(clazz)).append(";").toString();
    }


}
