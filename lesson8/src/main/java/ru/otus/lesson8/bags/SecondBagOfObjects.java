package ru.otus.lesson8.bags;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by piphonom
 */
public class SecondBagOfObjects {
    List<Integer> list = new ArrayList<>();
    int[] array = {1,2,3};
    FirstBagOfObjects[] bag = {new FirstBagOfObjects(), new FirstBagOfObjects()};
    byte b = 1;

    public SecondBagOfObjects() {
        list.add(1);
        list.add(2);
        list.add(3);
    }
}
