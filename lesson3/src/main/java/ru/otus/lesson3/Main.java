package ru.otus.lesson3;

import java.util.*;

/**
 * Created by piphonom on 16.04.17.
 */
public class Main {
    public static void main(String[] args) {
        String blabla = "the string";
        char[] bc = blabla.toCharArray();
        Map<Character, Integer> quantity = new HashMap<>();
        for (Character c : bc) {
            quantity.put(c, quantity.get(c) != null ? quantity.get(c) + 1 : 1);
        }

        for (Character c : quantity.keySet()) {
            System.out.println(c + ": " + quantity.get(c));
        }

        String[] initial = new String[15];
        List<String> nativeArrayList = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            initial[i] = "The string number " + i;
            nativeArrayList.add("");
        }

        List<String> myList = new MyArrayList<>();
        Collections.addAll(myList, initial);

        Collections.copy(nativeArrayList, myList);

        List<String> myList2 = new MyArrayList<>(nativeArrayList);
        Collections.shuffle(myList2);
        Collections.sort(myList2);
    }
}
