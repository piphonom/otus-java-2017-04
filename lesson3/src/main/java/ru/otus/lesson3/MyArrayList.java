package ru.otus.lesson3;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;

/**
 * Created by piphonom on 16.04.17.
 */
public class MyArrayList<E> implements List<E> {

    private final int INITIAL_SIZE = 10;
    private final int INCREASE_TRESHOLD = Integer.MAX_VALUE / 2;

    /* Internal array to store objects */
    private Object[] storageArray;
    /* Explicit storageSize of internal array */
    private int storageSize = 0;
    /* Number of elements */
    private int refNumber = 0;

    MyArrayList() {
        this.storageSize = INITIAL_SIZE;
        storageArray = new Object[storageSize];
    }

    MyArrayList(int size) {
        if (size == 0) {
            storageSize = INITIAL_SIZE;
        } else {
            storageSize = size;
        }
        storageArray = new Object[storageSize];
    }

    MyArrayList(Collection<? extends E> c) {
        storageSize = c.size();
        storageArray = Arrays.copyOf(c.toArray(), storageSize);
        refNumber = storageSize;
    }

    public int size() {
        return refNumber;
    }

    public boolean isEmpty() {
        return (refNumber == 0);
    }

    public boolean contains(Object o) {
        /* o==null ? e==null : o.equals(e) */
        if (o == null)
            throw new NullPointerException();
        for (int i = 0; i < refNumber; i++) {
            if (storageArray[i].equals(o))
                return true;
        }
        return false;
    }

    public Object[] toArray() {
        return Arrays.copyOf(storageArray, refNumber);
    }

    public <T> T[] toArray(T[] a) {
        if (a == null)
            throw new NullPointerException();
        if (refNumber <= a.length) {
            System.arraycopy(storageArray, 0, a, 0, refNumber);
            if (refNumber < a.length)
                a[refNumber] = null;
            return a;
        } else {
            return (T[]) Arrays.copyOf(storageArray, refNumber, a.getClass());
        }
    }

    private void increaseStorageIfNeeded(int numObjects) {
        if ((Integer.MAX_VALUE - refNumber) < numObjects)
            throw new OutOfMemoryError("Too many objects");
        int neededCapacity = refNumber + numObjects;
        if (neededCapacity > storageSize) {
            if (storageSize == Integer.MAX_VALUE)
                throw new OutOfMemoryError("Unable to realloc memory");
            /* Need to increase the storageSize */
            int newSize = storageSize;
            do {
                newSize = newSize < INCREASE_TRESHOLD ? 2 * newSize : Integer.MAX_VALUE;
            } while (neededCapacity > newSize);
            storageArray = Arrays.copyOf(storageArray, newSize);
            storageSize = newSize;
        }
    }

    private void checkIndex(int index) {
        if (index < 0)
            throw new IndexOutOfBoundsException();
        if (refNumber < index) {
            throw new IndexOutOfBoundsException();
        }
    }

    public boolean add(E e) {
        if (e == null)
            /* This list does not permit null elements */
            throw new NullPointerException();

        increaseStorageIfNeeded(1);
        storageArray[refNumber++] = e;

        return false;
    }

    public void add(int index, E element) {
        if (element == null)
            /* This list does not permit null elements */
            throw new NullPointerException();

        checkIndex(index);
        increaseStorageIfNeeded(1);

        System.arraycopy(storageArray, index, storageArray, index + 1, refNumber - index);
        storageArray[index] = element;
        refNumber++;
    }

    public boolean remove(Object o) {
        int index = indexOf(o);
        if (index >= 0) {
            remove(index);
            return true;
        }
        return false;
    }

    public E remove(int index) {
        checkIndex(index);
        E removed = (E) storageArray[index];
        refNumber--;
        if (refNumber > 0) {
            System.arraycopy(storageArray, index + 1, storageArray, index, refNumber - index);
            storageArray[refNumber] = null;
        }

        return removed;
    }

    public boolean containsAll(Collection<?> c) {
        if (c == null)
            return false;
        for (Object o : c)
            if (!contains(o))
                return false;
        return true;
    }

    /*
    private boolean addAll(int index, Object[] objects) {
        increaseStorageIfNeeded(objects.length);
        // check for null ?????
        System.arraycopy(objects, 0, storageArray, index, objects.length);
        return true;
    }
    */

    public boolean addAll(Collection<? extends E> c) {

        if (c == null)
            return false;
        //return addAll(refNumber, c.toArray());

        for (E e : c) {
            add(e);
        }
        return true;
    }

    public boolean addAll(int index, Collection<? extends E> c) {
        if (c == null)
            return false;
        int i = index;
        //return addAll(index, c.toArray());
        for (E e : c) {
            add(i++, e);
        }
        return true;
    }

    public boolean removeAll(Collection<?> c) {
        throw new NotImplementedException();
    }

    public boolean retainAll(Collection<?> c) {
        throw new NotImplementedException();
    }

    public void clear() {
        for (int i = 0; i < refNumber; i++) {
            storageArray[i] = null;
        }
        refNumber = 0;
    }

    public E get(int index) {
        checkIndex(index);

        return (E)storageArray[index];
    }

    public E set(int index, E element) {
        if (element == null)
            /* This list does not permit null elements */
            throw new NullPointerException();

        checkIndex(index);

        E old = (E)storageArray[index];
        storageArray[index] = element;

        return old;
    }

    public int indexOf(Object o) {
        if (o == null)
            /* This list does not permit null elements */
            throw new NullPointerException();
        for (int i = 0; i < refNumber; i++) {
            if (storageArray[i].equals(o))
                return i;
        }
        return -1;
    }

    public int lastIndexOf(Object o) {
        if (o == null)
            /* This list does not permit null elements */
            throw new NullPointerException();
        for (int i = refNumber - 1; i >= 0; i--) {
            if (storageArray[i].equals(o))
                return i;
        }
        return -1;
    }

    public Iterator<E> iterator() {
        return new MyListIterator();
    }

    public ListIterator<E> listIterator() {
        return new MyListIterator();
    }

    public ListIterator<E> listIterator(int index) {
        return new MyListIterator(index);
    }

    public List<E> subList(int fromIndex, int toIndex) {
        throw new NotImplementedException();
    }

    private class MyListIterator implements ListIterator<E> {

        private int cursor = 0;
        private int cursorState = -1;

        MyListIterator() {

        }

        MyListIterator(int index) {
            checkIndex(index);
            cursor = index;
        }

        public boolean hasNext() {
            if (cursor == refNumber)
                return false;
            return true;
        }

        public boolean hasPrevious() {
            if (cursor == 0)
                return false;
            else
                return true;
        }

        public E next() {
            if (cursor == refNumber) {
                cursorState = -1;
                throw new NoSuchElementException();
            }
            cursorState = cursor;
            return (E)storageArray[cursor++];
        }

        public E previous() {
            if (cursor == 0) {
                cursorState = -1;
                throw new NoSuchElementException();
            }
            cursorState = --cursor;
            return (E)storageArray[cursor];
        }

        public int nextIndex() {
            return cursor;
        }

        public int previousIndex() {
            return cursor - 1;
        }

        public void remove() {
            if (cursorState != -1) {
                MyArrayList.this.remove(cursorState);
                cursorState = -1;
            } else {
                throw new IllegalStateException();
            }
        }

        public void set(E e) {
            if (cursorState != -1) {
                MyArrayList.this.set(cursorState, e);
                cursorState = -1;
            } else {
                throw new IllegalStateException();
            }
        }

        public void add(E e) {
            if (cursorState != -1) {
                MyArrayList.this.add(e);
                cursorState = -1;
            } else {
                throw new IllegalStateException();
            }
        }
    }
}
