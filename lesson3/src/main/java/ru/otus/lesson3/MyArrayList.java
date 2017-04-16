package ru.otus.lesson3;

import java.util.*;

/**
 * Created by piphonom on 16.04.17.
 */
public class MyArrayList<E> implements List<E> {

    private final int INITIAL_SIZE = 10;

    /* Internal array to store objects */
    private Object[] storageArray;
    /* Explicit storageSize of internal array */
    private int storageSize = 0;
    /* Number of elements in array */
    private int refNumber = 0;

    MyArrayList() {
        this.storageSize = INITIAL_SIZE;
        storageArray = new Object[storageSize];
    }

    MyArrayList(int size) {
        if (size == 0) {
            storageArray = new Object[INITIAL_SIZE];
        } else if (size > Integer.MAX_VALUE) {
            storageSize = Integer.MAX_VALUE;
        } else {
            storageSize = size;
        }
        storageArray = new Object[storageSize];
    }

    public int size() {
        return refNumber;
    }

    public boolean isEmpty() {
        return (refNumber == 0);
    }

    public boolean contains(Object o) {
        /* o==null ? e==null : o.equals(e) */
        return false;
    }

    public Iterator<E> iterator() {
        return new MyListIterator();
    }

    public Object[] toArray() {
        return Arrays.copyOf(storageArray, refNumber);
    }

    public <T> T[] toArray(T[] a) {
        if (refNumber <= a.length) {
            System.arraycopy(storageArray, 0, a, 0, refNumber);
            if (refNumber < a.length)
                a[refNumber] = null;
            return a;
        } else {
            return (T[]) Arrays.copyOf(storageArray, refNumber, a.getClass());
        }
    }

    public boolean add(E e) {
        if (refNumber == storageSize) {
            if (storageSize == Integer.MAX_VALUE)
                throw new OutOfMemoryError("No memory for realloc");
            /* Need to increase the storageSize */
            storageSize = (2 * storageSize) < Integer.MAX_VALUE ? 2 * storageSize : Integer.MAX_VALUE;
            storageArray = Arrays.copyOf(storageArray, storageSize);
        }
        storageArray[refNumber++] = e;

        return false;
    }

    public boolean remove(Object o) {
        int index = indexOf(o);
        if (index >= 0) {
            remove(index);
            return true;
        }
        return false;
    }

    public boolean containsAll(Collection<?> c) {
        return false;
    }

    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    public boolean addAll(int index, Collection<? extends E> c) {
        return false;
    }

    public boolean removeAll(Collection<?> c) {
        return false;
    }

    public boolean retainAll(Collection<?> c) {
        return false;
    }

    public void clear() {

    }

    public E get(int index) {
        return null;
    }

    public E set(int index, E element) {
        return null;
    }

    public void add(int index, E element) {

    }

    public E remove(int index) {
        return null;
    }

    public int indexOf(Object o) {
        return 0;
    }

    public int lastIndexOf(Object o) {
        return 0;
    }

    public ListIterator<E> listIterator() {
        return null;
    }

    public ListIterator<E> listIterator(int index) {
        return null;
    }

    public List<E> subList(int fromIndex, int toIndex) {
        return null;
    }

    private class MyListIterator implements ListIterator<E> {

        public boolean hasNext() {
            return false;
        }

        public E next() {
            return null;
        }

        public boolean hasPrevious() {
            return false;
        }

        public E previous() {
            return null;
        }

        public int nextIndex() {
            return 0;
        }

        public int previousIndex() {
            return 0;
        }

        public void remove() {

        }

        public void set(E e) {

        }

        public void add(E e) {

        }
    }
}
