package com.stclair.corlib.collection;


import java.util.*;

import static com.stclair.corlib.validation.Validation.inRange;

public class ResolvableSequence<T> implements Collection<T> {

    List<Resolvable<T>> resolvables;

    public ResolvableSequence(Collection<Resolvable<T>> resolvables) {
        this.resolvables = new ArrayList<>(resolvables);
    }

    @Override
    public int size() {
        return resolvables.size();
    }

    @Override
    public boolean isEmpty() {
        return resolvables.isEmpty();
    }

    @Override
    public Iterator<T> iterator() {

        return new ResolvableIterator(resolvables.iterator());
    }

    @Override
    public Object[] toArray() {

        throw new UnsupportedOperationException();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        throw new UnsupportedOperationException();
    }


    public T get(int index) {

        inRange(index, 0, resolvables.size() - 1, "index");

        int lastResolved = index;

        while (lastResolved >= 0 && ! resolvables.get(lastResolved).isResolved())
            lastResolved--;

        while (lastResolved < index)
            resolvables.get(lastResolved++).resolve();

        return resolvables.get(index).getValue();
    }


    class ResolvableIterator implements Iterator<T> {

        Iterator<Resolvable<T>> inner;

        public ResolvableIterator(Iterator<Resolvable<T>> inner) {
            this.inner = inner;
        }

        @Override
        public boolean hasNext() {
            return inner.hasNext();
        }

        @Override
        public T next() {
            return inner.next().getValue();
        }
    }


    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(T t) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }
}
