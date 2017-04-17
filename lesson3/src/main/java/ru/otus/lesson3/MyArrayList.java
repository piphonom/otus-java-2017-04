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
    /* Number of elements */
    private int refNumber = 0;
    /* number of null elements */
    private int refNullNumber = 0;

    MyArrayList() {
        this.storageSize = INITIAL_SIZE;
        storageArray = new Object[storageSize];
    }

    MyArrayList(int size) {
        if (size == 0) {
            storageSize = INITIAL_SIZE;
        } else if (size > Integer.MAX_VALUE) {
            storageSize = Integer.MAX_VALUE;
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
            return (refNullNumber == 0);
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
        int neededCapacity = refNumber + numObjects;
        if (neededCapacity > storageSize) {
            if (storageSize == Integer.MAX_VALUE)
                throw new OutOfMemoryError("Unable to realloc memory");
            /* Need to increase the storageSize */
            long newSize = storageSize;
            do {
                newSize *= Math.min(2 * newSize, Integer.MAX_VALUE);
                if (newSize == Integer.MAX_VALUE && neededCapacity > newSize)
                    throw new OutOfMemoryError("Too many objects");;
            } while (neededCapacity > newSize);
            storageArray = Arrays.copyOf(storageArray, (int)newSize);
            storageSize = (int)newSize;
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
        increaseStorageIfNeeded(1);
        storageArray[refNumber++] = e;
        if (e == null)
            refNullNumber++;

        return false;
    }

    public void add(int index, E element) {
        checkIndex(index);
        increaseStorageIfNeeded(1);

        System.arraycopy(storageArray, index, storageArray, index + 1, refNumber - index);
        storageArray[index] = element;
        refNumber++;
        if (element == null)
            refNullNumber++;
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
        if (removed == null)
            refNullNumber--;

        return removed;
    }

    public boolean containsAll(Collection<?> c) {
        if (c == null)
            return false;
        for (Object o : c) {
            if (contains(o) == false)
                return false;
        }
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
        return false;
    }

    public boolean retainAll(Collection<?> c) {
        return false;
    }

    public void clear() {
        for (int i = 0; i < refNumber; i++) {
            storageArray[i] = null;
        }
        refNumber = refNullNumber = 0;
    }

    public E get(int index) {
        checkIndex(index);

        return (E)storageArray[index];
    }

    public E set(int index, E element) {
        checkIndex(index);

        E old = (E)storageArray[index];
        storageArray[index] = element;
        if (old == null && element != null)
            refNullNumber--;
        return old;
    }

    public int indexOf(Object o) {
        if (o == null) {
            if (refNullNumber == 0) {
                return -1;
            }
            for (int i = 0; i < refNumber; i++) {
                if (storageArray[i] == null)
                    return i;
            }
            return -1;
        }
        for (int i = 0; i < refNumber; i++) {
            if (storageArray[i].equals(o))
                return i;
        }
        return -1;
    }

    public int lastIndexOf(Object o) {
        if (o == null) {
            if (refNullNumber == 0)
                return -1;
            for (int i = refNumber - 1; i >= 0; i--) {
                if (storageArray[i] == null)
                    return i;
            }
            return -1;
        }
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
        return null;
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
