package ru.otus.lesson4;

/**
 * Created by piphonom on 23.04.17.
 */
public class ChunkSize implements ChunkSizeMBean {
    private int size;

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void setSize(int size) {
        this.size = size;
    }
}
