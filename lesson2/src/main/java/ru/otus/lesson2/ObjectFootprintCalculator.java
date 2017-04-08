package ru.otus.lesson2;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Created by piphonom on 06.04.17.
 */
public class ObjectFootprintCalculator {

    /*
    * TODO: get arch dependent memory layout
    * */
    /*
    * http://btoddb-java-sizing.blogspot.ru/
    * https://github.com/jbellis/jamm/blob/master/src/org/github/jamm/MemoryLayoutSpecification.java
    * */
    private final static int headerSize = 12;
    private final static int arrayHeaderSize = 16;
    private final static int referenceSize = 4;
    private final static int paddingSize = 8;
    private final static int superFieldsPadding = 4;

    private long objectSize = 0;
    /*
    * List of already unrolled objects
    * There is used Identity because we want to compare objects references not objects contents
    */
    private Set<Object> unrolledObjects = Collections.newSetFromMap(new IdentityHashMap<Object, Boolean>());

    /*
    * Cache of already processed Class objects
    * */
    private Map<String, ClassInfo> ClassInfos = new HashMap<>();

    /*
    * To use Singletone pattern
    * */
    private static final ObjectFootprintCalculator Instance = new ObjectFootprintCalculator();
    private ObjectFootprintCalculator() {

    }
    public static ObjectFootprintCalculator getInstance() {
        return Instance;
    }

    /*
    * Main entry to get footprint
    * */
    public long getFootprint(Object object) {
        try {
            unrollObject(object);
            return objectSize;
        }
        finally {
            objectSize = 0;
            unrolledObjects.clear();
        }
    }

    private void unrollObject(Object object) {

        /* Some sort of protection */
        if (object == null)
            return;

        /* Check if we already unrolled this object */
        if (unrolledObjects.contains(object))
           return;

        /* Add object to prevent multiple unrollments */
        unrolledObjects.add(object);

        /* Get source of metainfo */
        Class<?> clazz = object.getClass();

        /* It is special case when it is array */
        if (clazz.isArray()) {
            objectSize += roundTo(arrayHeaderSize, paddingSize);
            /* For primitives we just calculate total ammount of memory to store them */
            if (clazz.getComponentType().isPrimitive()) {
                long notPaddedSize = getPrimitiveFieldSize(clazz.getComponentType()) * Array.getLength(object);
                objectSize += roundTo(notPaddedSize, paddingSize);
            }
            /* For other objects we need to calculate ammount of memory to store references and
             * then we unroll the objects recursively */
            else {
                objectSize += referenceSize * Array.getLength(object);
                Object[] arrayObjects = (Object[]) object;
                for (Object o : arrayObjects) {
                    unrollObject(o);
                }
            }

            return;
        }

        /* Check if we already processed metainfo for this class */
        if (!ClassInfos.containsKey(clazz.getName())) {
            /* If no then process metainfo. It will be pushed into ClassInfos automatically */
            new ClassInfo(clazz);
        }
        ClassInfo info = ClassInfos.get(clazz.getName());
        if (info != null) {
            /* Get object's footprint */
            objectSize += info.getObjectSize();

            /* Get list of referenced objects to mesure them's footprints */
            Field[] fields = info.getObjectFields();
            for (Field f : fields) {
                if (f.isAccessible()) {
                    try {
                        Object referencedObject = f.get(object);
                        if (referencedObject != null) {
                            /* Unroll field objects recursively */
                            unrollObject(referencedObject);
                        }
                    } catch(IllegalAccessException e){
                        System.out.println("Access denied to field " + f.getName() + " of object " + clazz.getName());
                    }
                } else {
                    System.out.println("Access denied to field " + f.getName() + " of object " + clazz.getName());
                }
            }
        }
    }

    /* Used to align objects memory footprint to desired padding */
    private static long roundTo(long x, int multiple) {
        return ((x + multiple - 1) / multiple) * multiple;
    }

    /* Get primitives size without padding */
    private static long getPrimitiveFieldSize(Class<?> type) {
        if (type == boolean.class || type == byte.class) {
            return 1;
        }
        if (type == char.class || type == short.class) {
            return 2;
        }
        if (type == int.class || type == float.class) {
            return 4;
        }
        if (type == long.class || type == double.class) {
            return 8;
        }
        throw new AssertionError("Encountered unexpected primitive type " +
                type.getName());
    }

    /*
    * This class is used to store information about different Class objects that describes different Object_s
    * */
    private class ClassInfo {
        private long objectSize = 0;
        private long fieldsSize = 0;
        private String className;
        private Field[] objectFields;

        ClassInfo(Class<?> clazz) {
            className = clazz.getName();
            System.out.println("Get info for " + className);

            List<Field> fields = new LinkedList<>();
            /* Get references for all non static fields (accessible or not) */
            for (Field field : clazz.getDeclaredFields()) {
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }

                /* In case of primitives just get the size */
                if (field.getType().isPrimitive()) {
                    fieldsSize += getPrimitiveFieldSize(field.getType());
                    continue;
                }

                fieldsSize += referenceSize;
                /* To make possible to get Object from reference */
                field.setAccessible(true);
                fields.add(field);
            }
            /* Recursively get info from SuperClass */
            Class<?> superClazz = clazz.getSuperclass();
            if (superClazz != null) {
                String superClassName = superClazz.getName();
                if (!ClassInfos.containsKey(superClassName)) {
                    new ClassInfo(superClazz);
                }
                ClassInfo superClassInfo = ClassInfos.get(superClassName);
                fieldsSize += roundTo(superClassInfo.getFieldsSize(), superFieldsPadding);
                fields.addAll(Arrays.asList(superClassInfo.getObjectFields()));
            }

            objectSize = roundTo(fieldsSize + headerSize, paddingSize);

            /* Save fields references into array for memory optimization */
            objectFields = fields.toArray(new Field[fields.size()]);
            /* Store himself into ClassInfos cache */
            ClassInfos.put(className, this);
        }

        /*
        * Getters
        * */
        public long getObjectSize() {
            return objectSize;
        }

        public long getFieldsSize() {
            return fieldsSize;
        }

        public String getClassName() {
            return className;
        }

        public Field[] getObjectFields() {
            return objectFields;
        }
    }
}
