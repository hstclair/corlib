package com.stclair.corlib.collection;

import java.util.function.Supplier;

public class Resolvable<T> {

    T value;

    boolean resolved = false;

    public Supplier<T> resolver;

    public Resolvable(Supplier<T> resolver) {
        this.resolver = resolver;
    }

    public boolean isResolved() {
        return resolved;
    }

    public void resolve() {

        if (! resolved) {
            value = resolver.get();
            resolved = true;
            resolver = null;
        }
    }

    public T getValue() {

        resolve();

        return value;
    }
}
