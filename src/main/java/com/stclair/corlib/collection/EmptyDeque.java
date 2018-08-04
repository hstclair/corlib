package com.stclair.corlib.collection;

import com.sun.security.auth.UnixNumericGroupPrincipal;

import java.util.*;

public class EmptyDeque<X> implements Deque<X> {

    @Override
    public void addFirst(X x) {
        throw new IllegalStateException();
    }

    @Override
    public void addLast(X x) {
        throw new IllegalStateException();
    }

    @Override
    public boolean offerFirst(X x) {
        return false;
    }

    @Override
    public boolean offerLast(X x) {
        return false;
    }

    @Override
    public X removeFirst() {
        throw new NoSuchElementException();
    }

    @Override
    public X removeLast() {
        throw new NoSuchElementException();
    }

    @Override
    public X pollFirst() {
        return null;
    }

    @Override
    public X pollLast() {
        return null;
    }

    @Override
    public X getFirst() {
        throw new NoSuchElementException();
    }

    @Override
    public X getLast() {
        throw new NoSuchElementException();
    }

    @Override
    public X peekFirst() {
        return null;
    }

    @Override
    public X peekLast() {
        return null;
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        return false;
    }

    @Override
    public boolean add(X x) {
        throw new IllegalStateException();
    }

    @Override
    public boolean offer(X x) {
        return false;
    }

    @Override
    public X remove() {
        throw new NoSuchElementException();
    }

    @Override
    public X poll() {
        return null;
    }

    @Override
    public X element() {
        throw new NoSuchElementException();
    }

    @Override
    public X peek() {
        return null;
    }

    @Override
    public boolean addAll(Collection<? extends X> c) {
        throw new IllegalStateException();
    }

    @Override
    public void push(X x) {
        throw new IllegalStateException();
    }

    @Override
    public X pop() {
        throw new NoSuchElementException();
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Iterator<X> iterator() {
        return Collections.emptyIterator();
    }

    @Override
    public Iterator<X> descendingIterator() {
        return Collections.emptyIterator();
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return Arrays.copyOf(a, 0);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }
}
