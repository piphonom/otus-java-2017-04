package ru.otus.lesson2;

/**
 * Created by piphonom on 06.04.17.
 */
public class Main {
    public static void main(String[] args) {
        ObjectFootprintCalculator footprintCalc = ObjectFootprintCalculator.getInstance();
        String string = "this is test string. Ho ho ho";
        String arrayOfStrings[] = new String[10];
        long stringSize = footprintCalc.getFootprint(string);
        long arrayOfStringsSize = footprintCalc.getFootprint(arrayOfStrings);
        System.out.println("Footprint of test String is " + stringSize + " bytes");
        System.out.println("Footprint of empty String array is " + arrayOfStringsSize + " bytes");
        arrayOfStrings[0] = string;
        arrayOfStringsSize = footprintCalc.getFootprint(arrayOfStrings);
        System.out.println("Footprint of String array with one ref is " + arrayOfStringsSize + " bytes");
    }
}
