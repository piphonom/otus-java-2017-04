package ru.otus.lesson2;

import org.github.jamm.MemoryMeter;

/**
 * Created by piphonom on 06.04.17.
 */
public class Main {
    public static void main(String[] args) {
        /*
        * JVM params: -XX:+UseCompressedOops
        * */
        ObjectFootprintCalculator footprintCalc = ObjectFootprintCalculator.getInstance();
        long objectSize = footprintCalc.getFootprint(new Object());
        System.out.println("Footprint of Object is " + objectSize + " bytes");
        String string = "";
        long stringSize = footprintCalc.getFootprint(string);
        System.out.println("Footprint of empty String is " + stringSize + " bytes");
        string = "this is test string. Ho ho ho";
        stringSize = footprintCalc.getFootprint(string);
        System.out.println("Footprint of String with " + string.length() + " characters is " + stringSize + " bytes");
        String arrayOfStrings[] = new String[10];
        long arrayOfStringsSize = footprintCalc.getFootprint(arrayOfStrings);
        System.out.println("Footprint of empty String array is " + arrayOfStringsSize + " bytes");
        arrayOfStrings[0] = string;
        arrayOfStringsSize = footprintCalc.getFootprint(arrayOfStrings);
        System.out.println("Footprint of String array with one ref is " + arrayOfStringsSize + " bytes");

        /* Use instrumentation to check results */
        /*
        * JVM params: -XX:+UseCompressedOops -javaagent:<path to>/jamm-0.3.1.jar
        * */
        MemoryMeter meter = new MemoryMeter();
        System.out.println("Instrumentation check: ");
        objectSize = meter.measureDeep(new Object());
        System.out.println("Footprint of Object is " + objectSize + " bytes");
        stringSize = meter.measureDeep(new String(""));
        System.out.println("Footprint of empty String is " + stringSize + " bytes");
        stringSize = meter.measureDeep(string);
        System.out.println("Footprint of String with " + string.length() + " characters is " + stringSize + " bytes");
        String arrayOfStringsForInsts[] = new String[10];
        arrayOfStringsSize = meter.measureDeep(arrayOfStringsForInsts);
        System.out.println("Footprint of empty String array is " + arrayOfStringsSize + " bytes");
        arrayOfStringsForInsts[0] = string;
        arrayOfStringsSize = meter.measureDeep(arrayOfStringsForInsts);
        System.out.println("Footprint of String array with one ref is " + arrayOfStringsSize + " bytes");
    }
}
