package ru.otus.lesson8;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Created by piphonom
 */
public class JSONObjectWriter {

    private static Map<String, ClassInfo> ClassInfos = new HashMap<>();

    private JSONObjectWriter() {}

    public static String toJson(Object object) {

        JSONObjectWriter objectWriter = new JSONObjectWriter();
        JsonObjectBuilder builder = Json.createObjectBuilder();
        objectWriter.convertObject(null, object, builder);

        StringWriter stringWriter = new StringWriter();
        JsonWriter writer = Json.createWriter(stringWriter);
        writer.writeObject(builder.build());
        writer.close();
        return stringWriter.getBuffer().toString();
    }

    private void convertObject(String name, Object object, JsonObjectBuilder builder) {
        if (object instanceof Collection<?>) {
            Object array = normalizeCollection((Collection)object);
            if (array == null)
                return;
            object = array;
        }
        Class<?> clazz = object.getClass();

        if (Modifier.isTransient(clazz.getModifiers()))
            return;

        if (clazz.isArray()) {
            processArrayObject(name, object, builder);
        } else {
            processSimpleObject(name, object, builder);
        }
    }

    private Object normalizeCollection(Collection collection) {
        int length = collection.size();
        if (length != 0) {
            Object first = collection.iterator().next();
            Object array = Array.newInstance(first.getClass(), length);
            Iterator iterator = collection.iterator();
            for (int i = 0; i < length; i++) {
                Array.set(array, i, iterator.next());
            }
            return array;
        }
        return null;
    }

    private void processArrayObject(String name, Object object, JsonObjectBuilder builder) {
        Class<?> clazz = object.getClass();
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        String compTypeName = clazz.getComponentType().getSimpleName();
        for (int i = 0; i < Array.getLength(object); i++) {
            switch (compTypeName) {
                case "boolean":
                case "Boolean":
                    arrayBuilder.add((boolean)Array.get(object, i));
                    break;
                case "byte":
                case "Byte":
                    arrayBuilder.add((byte)Array.get(object, i));
                    break;
                case "short":
                case "Short":
                    arrayBuilder.add((short)Array.get(object, i));
                    break;
                case "int":
                case "Integer":
                    arrayBuilder.add((int)Array.get(object, i));
                    break;
                case "float":
                case "Float":
                    arrayBuilder.add((float)Array.get(object, i));
                    break;
                case "double":
                case "Double":
                    arrayBuilder.add((double)Array.get(object, i));
                    break;
                case "String":
                    arrayBuilder.add((String) Array.get(object, i));
                    break;
                default:
                    JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                    Object parsedObject = Array.get(object, i);
                    convertObject(null, parsedObject, objectBuilder);
                    arrayBuilder.add(objectBuilder);
                    break;
            }
        }
        if (name == null) {
            //builder.addAll(arrayBuilder);
        } else {
            builder.add(name, arrayBuilder);
        }
    }

    private void processSimpleObject(String name, Object object, JsonObjectBuilder builder) {
        Class<?> clazz = object.getClass();
        String typeName = clazz.getSimpleName();
        switch (typeName) {
            case "Boolean":
                builder.add(name, (boolean)object);
                break;
            case "Byte":
                builder.add(name, (byte)object);
                break;
            case "Short":
                builder.add(name, (short)object);
                break;
            case "Integer":
                builder.add(name, (int)object);
                break;
            case "Float":
                builder.add(name, (float)object);
                break;
            case "Double":
                builder.add(name, (double)object);
                break;
            case "String":
                builder.add(name, (String) object);
                break;
            default:
                if (!ClassInfos.containsKey(clazz.getName())) {
                    new ClassInfo(clazz);
                }
                ClassInfo info = ClassInfos.get(clazz.getName());
                if (info != null) {
                    Field[] fields = info.getObjectFields();
                    JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                    for (Field f : fields) {
                        if (f.isAccessible()) {
                            String fieldName = f.getName();
                            try {
                                convertObject(fieldName, f.get(object), objectBuilder);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if (name == null) {
                        builder.addAll(objectBuilder);
                    } else {
                        builder.add(name, objectBuilder);
                    }
                }
                break;
        }
    }

    private class ClassInfo {
        private String className;
        private Field[] objectFields;

        ClassInfo(Class<?> clazz) {
            className = clazz.getName();

            List<Field> fields = new ArrayList<>(clazz.getDeclaredFields().length);

            for (Field field : clazz.getDeclaredFields()) {
                if(Modifier.isTransient(field.getModifiers())) {
                    continue;
                }
                /*if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }*/

                field.setAccessible(true);
                fields.add(field);
            }
            Class<?> superClazz = clazz.getSuperclass();
            if (superClazz != null) {
                String superClassName = superClazz.getName();
                if (!ClassInfos.containsKey(superClassName)) {
                    new ClassInfo(superClazz);
                }
                ClassInfo superClassInfo = ClassInfos.get(superClassName);
                fields.addAll(Arrays.asList(superClassInfo.getObjectFields()));
            }

            objectFields = fields.toArray(new Field[fields.size()]);
            ClassInfos.put(className, this);
        }

        public String getClassName() {
            return className;
        }

        public Field[] getObjectFields() {
            return objectFields;
        }
    }
}
