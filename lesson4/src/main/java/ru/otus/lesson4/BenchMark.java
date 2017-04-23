package ru.otus.lesson4;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by piphonom on 23.04.17.
 */
public class BenchMark {
    private BenchMark() {}
    public static void run(ChunkSize chunkSize, SleepTimeout sleepTimeout, Logger logger) {
        List<Object> objectsList = new ArrayList<>();

        while (true) {
            int cs = chunkSize.getSize();
            Object[] objectsArray = new Object[cs];
            for (int i = 0; i < cs; i++) {
                objectsArray[i] = new String(new char[0]);
            }
            Collections.addAll(objectsList, objectsArray);
            try {
                Thread.sleep(sleepTimeout.getTimeout());
            } catch (InterruptedException e) {

            }
        }
    }
}
