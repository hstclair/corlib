package com.stclair.corlib.topology;

public class RecursionException extends RuntimeException {

    Object parent;

    Object instance;

    public RecursionException(String message, Object parent, Object instance) {

        super(message);

        this.parent = parent;
        this.instance = instance;
    }

    public Object getParent() {
        return parent;
    }

    public Object getInstance() {
        return instance;
    }
}
